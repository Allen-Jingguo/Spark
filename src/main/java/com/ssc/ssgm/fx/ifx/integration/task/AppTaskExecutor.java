package com.ssc.ssgm.fx.ifx.integration.task;


import com.ssc.ssgm.fx.ifx.integration.core.flow.Flow;
import com.ssc.ssgm.fx.ifx.integration.core.flow.FlowContext;
import com.ssc.ssgm.fx.ifx.integration.core.flow.FlowStatus;
import com.ssc.ssgm.fx.ifx.integration.util.ExecutorUtil;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class AppTaskExecutor implements ApplicationContextAware, InitializingBean {

    ApplicationContext ac;

    @Autowired
    FlowContext flowContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.ac = applicationContext;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        init();
    }

    void init() {
        try {
            flowContext.initLoadFlows();
            List<Flow> defaultFlows = new ArrayList<>(flowContext.getFlowsMap().values());
            //execute flow
            if (defaultFlows != null && !defaultFlows.isEmpty()) {
                defaultFlows.forEach(defaultFlow -> {
                    ExecutorUtil.getAsyncTaskExecutor().submit(() -> {
                        if(defaultFlow.getFlowStatus()== FlowStatus.RUNNABLE){
                            defaultFlow.execute();
                        }
                    });
                });
            }
        } catch (Exception e) {
            log.error("Exception::", e);
        }

        ExecutorUtil.getScheduledExecutor().scheduleWithFixedDelay(() -> {
            try {
                flowContext.initLoadFlows();
                final val fixTimeLoadFlows = new ArrayList<>(flowContext.getFlowsMap().values());
                if (fixTimeLoadFlows != null && !fixTimeLoadFlows.isEmpty()) {
                    fixTimeLoadFlows.forEach(defaultFlow -> {
                        ExecutorUtil.getAsyncTaskExecutor().submit(() -> {
                            try {
                                if(defaultFlow.getFlowStatus()== FlowStatus.RUNNABLE){
                                    defaultFlow.execute();
                                }
                            } catch (Exception e) {
                                log.error("Exception::", e);
                            }
                        });
                    });
                }
            } catch (Exception e) {
                log.error("Exception::", e);
            }
        }, 20, 30, TimeUnit.SECONDS);
    }


}
