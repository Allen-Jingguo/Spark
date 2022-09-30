package com.ssc.ssgm.fx.ifx.integration.core.inbound;

import com.ssc.ssgm.fx.ifx.integration.api.JDBCExeContext;
import com.ssc.ssgm.fx.ifx.integration.core.config.InboundConfig;
import com.ssc.ssgm.fx.ifx.integration.util.ExecutorUtil;
import com.ssc.ssgm.fx.ifx.integration.util.KeyValueConfigLoadUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

@Data
@Slf4j
public class JDBCInbound implements Inbound {

    JdbcTemplate jdbcTemplate;

    InboundConfig inboundConfig;

    final AtomicBoolean stopFlag = new AtomicBoolean(false);

    JDBCExeContext exeContext;

    Object waitObject = new Object();

    BlockingQueue<List<Map<String, Object>>> dataQueue = new ArrayBlockingQueue(20);

    public JDBCInbound(InboundConfig inboundConfig) {
        this.inboundConfig = inboundConfig;
        this.init();
    }

    @Override
    public boolean hasNext() {
        if (stopFlag.get()) {
            return false;
        }
        List<Map<String, Object>> peek = this.blockPeek();
        log.info("hasNext return {}", peek != null);
        return peek != null;
    }

    List<Map<String, Object>> blockPeek() {
        List<Map<String, Object>> peek = null;
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
        return this.get();
    }


    @Override
    public byte[] get() {

        if (stopFlag.get()) {
            return null;
        }

        List<Map<String, Object>> take = null;
        try {
            take = dataQueue.take();
        } catch (InterruptedException e) {
            log.error("InterruptedException::", e);
        }

        if (take == null || take.isEmpty()) {
            this.get();
        }

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream out = new ObjectOutputStream(bos);
            out.writeObject(take);
        } catch (IOException e) {
            log.error("IOException::", e);
        }

        return bos.toByteArray();
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

    /**
     * to be optimized, use pool
     */
    void init() {
        final val config = inboundConfig.getProperties();
        final val properties = KeyValueConfigLoadUtil.loadConfig(inboundConfig.getProperties());
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(properties.get("driver").toString());
        dataSource.setUrl(properties.get("url").toString());
        dataSource.setUsername(properties.get("user").toString());
        dataSource.setPassword(properties.get("password").toString());
        this.jdbcTemplate = new JdbcTemplate(dataSource);
        this.exeContext = new JDBCExeContext();
        exeContext.setExeSql(properties.get("sql").toString());

        if("loop".equals(properties.get("period").toString())){
            exeContext.setPeriod("20");
            ExecutorUtil.getScheduledExecutor().scheduleAtFixedRate(this::run, 10, Long.valueOf(exeContext.getPeriod()), TimeUnit.SECONDS);
        }else{
            ExecutorUtil.getAsyncTaskExecutor().submit(this::run);
        }



    }

    /**
     * to be optimized, use multiple connection pools
     */
    private void run() {
        try {
            if (this.stopFlag.get()) {
                return;
            }
            List<Map<String, Object>> dataMap = jdbcTemplate.query(exeContext.getExeSql(), new RowMapper<Map<String, Object>>() {
                public Map<String, Object> mapRow(ResultSet result, int rowNum) throws SQLException {
                    Map<String, Object> row = null;
                    try {
                        row = new HashMap<>();
                        final val metaData = result.getMetaData();
                        for (int i = 1; i <= metaData.getColumnCount(); i++) {
                            final val columnLabel = metaData.getColumnLabel(i);
                            row.put(columnLabel, result.getObject(columnLabel));
                        }
                    } catch (SQLException e) {
                        log.error("Exception::", e);
                    }
                    return row;
                }
            });

            log.info("===jdbc inbound  :: query sql={}", exeContext.getExeSql());

            log.info("===jdbc inbound query data :: data={}", dataMap.toString());
            if (dataMap != null) {
                try {
                    dataQueue.put(dataMap);
                    synchronized (waitObject) {
                        waitObject.notifyAll();
                    }
                } catch (InterruptedException e2) {
                    log.error("InterruptedException::", e2);
                }
            }

        } catch (Exception e1) {
            log.error("Exception::", e1);
        }
    }

    @Override
    public void close() {
        this.stopFlag.set(true);
    }

}

