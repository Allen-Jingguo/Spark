package com.ssc.ssgm.fx.ifx.integration.core.outbound;

import com.ssc.ssgm.fx.ifx.integration.core.config.OutboundConfig;
import com.ssc.ssgm.fx.ifx.integration.util.ExecutorUtil;
import com.ssc.ssgm.fx.ifx.integration.util.KeyValueConfigLoadUtil;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
public class JDBCOutbound implements OutBound<String> {

    JdbcTemplate jdbcTemplate;

    OutboundConfig outboundConfig;

    final AtomicBoolean stopFlag = new AtomicBoolean(false);

    Object waitObject = new Object();

    BlockingQueue<String> dataQueue = new ArrayBlockingQueue(20);

    public JDBCOutbound(OutboundConfig outboundConfig) {
        this.outboundConfig = outboundConfig;
        this.init();
    }

    @Override
    public void put(String format) {

        if (stopFlag.get()) {
            return;
        }

        try {
            dataQueue.put(format);
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

    void init() {

        final val config = outboundConfig.getProperties();
        final val properties = KeyValueConfigLoadUtil.loadConfig(config);
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(properties.get("driver").toString());
        dataSource.setUrl(properties.get("url").toString());
        dataSource.setUsername(properties.get("user").toString());
        dataSource.setPassword(properties.get("password").toString());
        this.jdbcTemplate = new JdbcTemplate(dataSource);

        ExecutorUtil.getParallelTaskExecutor().submit(this::loopExecuteSql);

    }


    /**
     * to be optimized, use multiple connection pools
     */
    private void loopExecuteSql() {
        while (true) {
            try {
                if (stopFlag.get()&&this.dataQueue.size()==0) {
                    break;
                }
                String sql = this.blockTake();

                //async task execute sql
                ExecutorUtil.getAsyncTaskExecutor().submit(() -> {
                    try {
                        jdbcTemplate.execute(sql);
                        log.info("====JdbcOutbound  successfully executed sql , sql={}", sql);
                    } catch (DataAccessException e) {
                        log.error("ExecutionException::", e);
                    } catch (Exception e) {
                        log.error("ExecutionException::", e);
                    }
                });

            } catch (Exception e) {
                log.error("ExecutionException::", e);
            }

        }
    }

    private String blockTake() {
        String sql = null;
        try {
            sql = dataQueue.take();
        } catch (InterruptedException e) {
            log.error("InterruptedException::", e);
        }
        if (sql == null && StringUtils.isBlank(sql)) {
            this.blockTake();
        }
        return sql;
    }

}
