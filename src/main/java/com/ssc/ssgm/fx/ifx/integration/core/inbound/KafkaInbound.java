package com.ssc.ssgm.fx.ifx.integration.core.inbound;

import com.ssc.ssgm.fx.ifx.integration.core.config.InboundConfig;
import com.ssc.ssgm.fx.ifx.integration.util.ExecutorUtil;
import com.ssc.ssgm.fx.ifx.integration.util.KeyValueConfigLoadUtil;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
public class KafkaInbound implements Inbound {

    List<String> topics;
    InboundConfig inboundConfig;
    Consumer<String, String> consumer;

    final AtomicBoolean stopFlag = new AtomicBoolean(false);

    BlockingQueue<Object> dataQueue = new ArrayBlockingQueue(20);

    Object waitObject = new Object();

    public KafkaInbound(InboundConfig inboundConfig) {
        this.inboundConfig = inboundConfig;
        this.init();
    }

    private void init() {
        final val conigVal = inboundConfig.getProperties();
        final val properties = KeyValueConfigLoadUtil.loadConfig(inboundConfig.getProperties());

        Map<String, Object> props = new HashMap<>();
        // kafka url
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, properties.get("bootstrap.servers"));// "127.0.0.1:9092"
        // default group, some consumer will receive same message if there no config
        // groupId here
        props.put(ConsumerConfig.GROUP_ID_CONFIG, properties.get("group.id"));// "defaultGroup"
        // config if auto commit,default is true
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, Boolean.parseBoolean(properties.get("enable.auto.commit")));// true
        // auto commit frequency(ms)
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, properties.get("auto.commit.interval.ms"));// 100
        // session timeout
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, properties.get("session.timeout.ms"));// "15000"
        // way of key serial
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        // way of value serial
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        // the rule of offset
        // 1, early: if no offset, begin at 0, if have offset, begin at offset
        // 2, early: if no offset, begin at data which new produce, if have offset,
        // begin at offset
        // 3, none: if no offset, will throw exception, if have offset, begin at offset
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, properties.get("auto.offset.reset"));// "earliest"

        DefaultKafkaConsumerFactory<String, String> factory = new DefaultKafkaConsumerFactory<String, String>(props);

        ConcurrentKafkaListenerContainerFactory<String, String> containerFactory = new ConcurrentKafkaListenerContainerFactory<>();
        // config consumer factory
        containerFactory.setConsumerFactory(factory);
        // config count of thread in consumer group
        containerFactory.setConcurrency(Integer.parseInt(properties.get("concurrency")));// 3
        // config poll timeout
        containerFactory.getContainerProperties().setPollTimeout(Integer.parseInt(properties.get("poll-time-out")));// 3000
        // require config true if set up batch listener
        containerFactory.setBatchListener(Boolean.parseBoolean(properties.get("batch-listener")));// true

        consumer = factory.createConsumer();

        topics = Arrays.asList(properties.get("topics").split(","));
        ExecutorUtil.getScheduledExecutor().scheduleAtFixedRate(this::run, 10, Long.parseLong(properties.get("period")),
                TimeUnit.SECONDS);
    }

    /**
     * to be optimized, use listner
     */
    private void run() {
        ConsumerRecords<String, String> records = consumer.poll(5000L);
        if (records != null) {
            try {
                dataQueue.put(records);
                synchronized (waitObject) {
                    waitObject.notifyAll();
                }
            } catch (InterruptedException e2) {
                log.error("InterruptedException::", e2);
            }
        }
    }

    @Override
    public boolean hasNext() {
        if (stopFlag.get()) {
            return false;
        }
        Object peek = this.blockPeek();
        log.info("hasNext return {}", peek != null);
        return peek != null;
    }

    Object blockPeek() {
        Object peek = null;
        try {
            do {
                peek = dataQueue.peek();
                if (peek != null) {
                    return peek;
                }
                synchronized (waitObject) {
                    waitObject.wait();
                }
                peek = dataQueue.peek();
            } while (peek == null);
        } catch (InterruptedException e) {
            log.error("InterruptedException::", e);
        } catch (Exception e) {
            log.error("Exception::", e);
        }
        return peek;
    }

    @Override
    public byte[] next() {
        byte[] bs = null;
        ConsumerRecords<String, String> records = consumer.poll(5000L);
        List<Map<String,Object>> result = new ArrayList<>();
        ByteArrayOutputStream bos = null;
        ObjectOutputStream oos = null;

        try {
            if(!records.isEmpty()){
                for (ConsumerRecord<String, String> record : records) {
                    Map<String,Object> map = new HashMap<>();
                    map.put("offset",record.offset());
                    map.put("key",record.key());
                    map.put("value",record.value());
                    result.add(map);
                }
            }
            bos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(bos);
            oos.writeObject(result);
            bs = bos.toByteArray();
            oos.close();
            bos.close();
        } catch (IOException e) {
            log.error("Exception::", e);
        } finally {
            if (bos!=null){
                try {
                    bos.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            if (oos!=null){
                try {
                    oos.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return new byte[0];
    }

    @Override
    public byte[] get() {
        return null;
    }

    @Override
    public byte[] getWithTransaction() {
        return new byte[0];
    }

    @Override
    public void rollback() {

    }

    @Override
    public void commit() {

    }

    @Override
    public void close() {
        this.stopFlag.set(true);
    }
}
