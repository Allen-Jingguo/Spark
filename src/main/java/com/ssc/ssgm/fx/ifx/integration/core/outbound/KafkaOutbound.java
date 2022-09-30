package com.ssc.ssgm.fx.ifx.integration.core.outbound;

import com.ssc.ssgm.fx.ifx.integration.core.config.OutboundConfig;
import com.ssc.ssgm.fx.ifx.integration.util.ExecutorUtil;
import com.ssc.ssgm.fx.ifx.integration.util.KeyValueConfigLoadUtil;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.security.auth.SecurityProtocol;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
public class KafkaOutbound implements OutBound<Object> {

    String topic;
    OutboundConfig outboundConfig;
    KafkaTemplate<String, String> kafkaTemplate;
    final AtomicBoolean stopFlag = new AtomicBoolean(false);

    BlockingQueue<String> dataQueue = new ArrayBlockingQueue(20);

    public KafkaOutbound(OutboundConfig outboundConfig) {
        this.outboundConfig = outboundConfig;
        this.init();
    }

    private void init() {
        final val conigVal = outboundConfig.getProperties();
        final val properties = KeyValueConfigLoadUtil.loadConfig(outboundConfig.getProperties());

        Map<String, Object> props = new HashMap<>();
        // address
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, properties.get("bootstrap.servers"));// "localhost:9092"
        // disable retry
//        props.put(ProducerConfig.RETRIES_CONFIG, properties.get("retries"));// 0
//        // full sync
//        props.put(ProducerConfig.ACKS_CONFIG, properties.get("acks"));// all
//        // send() will be blocked when the producer space is not available,default 60s
//        props.put(ProducerConfig.MAX_BLOCK_MS_CONFIG, properties.get("max.block.ms"));// 6000
//        // the size batch process is 16384byte
//        props.put(ProducerConfig.BATCH_SIZE_CONFIG, properties.get("batch.size"));// 16384
//        // batch send, delay time is 10 ms, it will improve throughput capacity
//        props.put(ProducerConfig.LINGER_MS_CONFIG, properties.get("linger.ms"));// 10
//        // the record which producer are able to use the total memory
//        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, properties.get("buffer.memory"));// 40960
//        // the limit size of the message send,default is 1048576
//        props.put(ProducerConfig.MAX_REQUEST_SIZE_CONFIG, properties.get("max.request.size"));// 1048576
//        // key serializer way
//        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
//        // value serializer way
//        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
//        // compression type,none,lz4,gzip,snappy,default is none
//        props.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, properties.get("compression.type"));// "none"
        props.put("security.protocol", SecurityProtocol.PLAINTEXT.name);
        topic = properties.get("topic");

        DefaultKafkaProducerFactory<String, String> factory = new DefaultKafkaProducerFactory<>(props);

        kafkaTemplate = new KafkaTemplate<String, String>(factory);

        ExecutorUtil.getParallelTaskExecutor().submit(this::loopProduceMessage);
    }

    private void loopProduceMessage() {
        while (true) {
            try {
                if (stopFlag.get()) {
                    return;
                }
                String message = this.blockTake();
                // async send message
                sendMessageAsync(topic, message);

            } catch (Exception e) {
                log.error("ExecutionException::", e);
            }

        }

    }

    private String blockTake() {
        String message = null;
        try {
            message = dataQueue.take();
        } catch (InterruptedException e) {
            log.error("InterruptedException::", e);
        }
        if (message == null && StringUtils.isBlank(message)) {
            this.blockTake();
        }
        return message;
    }

    @Override
    public void put(Object format) {
        if (stopFlag.get()) {
            return;
        }

        try {
            dataQueue.put(format.toString());
            log.info("Outbound data queue size={}", dataQueue.size());
        } catch (InterruptedException e) {
            log.error("InterruptedException", e);
        }
    }

    @Override
    public void close() {
        stopFlag.set(true);
    }

    @Override
    public void putWithTransAction(Object format) {

    }

    @Override
    public void rollback() {

    }

    @Override
    public void commit() {

    }

    private void sendMessageSync(String topic, String message)
            throws InterruptedException, TimeoutException, ExecutionException {
        kafkaTemplate.send(topic, message).get(10, TimeUnit.SECONDS);
    }

    private void sendMessageSync(String topic, String key, String message)
            throws InterruptedException, TimeoutException, ExecutionException {
        kafkaTemplate.send(topic, key, message).get(10, TimeUnit.SECONDS);
    }

    private void sendMessageAsync(String topic, String message) {
        kafkaTemplate.send(topic, message).addCallback(new ListenableFutureCallback() {
            @Override
            public void onFailure(Throwable throwable) {
                log.info("fail" + throwable.getMessage());
            }

            @Override
            public void onSuccess(Object o) {
                log.info("success");
            }
        });
    }

    private void sendMessageAsync(String topic, String key, String message) {
        kafkaTemplate.send(topic, key, message).addCallback(new ListenableFutureCallback() {
            @Override
            public void onFailure(Throwable throwable) {
                log.info("fail" + throwable.getMessage());
            }

            @Override
            public void onSuccess(Object o) {
                log.info("success");
            }
        });
    }
}
