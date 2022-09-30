package com.ssc.ssgm.fx.ifx.integration.task;


import com.ssc.ssgm.fx.ifx.integration.core.flow.FlowContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

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
//        try {
//            List<Flow> defaultFlows = flowContext.loadFlows();
//            //execute flow
//            if (defaultFlows != null && !defaultFlows.isEmpty()) {
//                defaultFlows.forEach(defaultFlow -> {
//                    ExecutorUtil.getAsyncTaskExecutor().submit(() -> {
//                        defaultFlow.start();
//                    });
//                });
//            }
//        } catch (Exception e) {
//            log.error("Exception::", e);
//        }
//
//        ExecutorUtil.getScheduledExecutor().scheduleWithFixedDelay(() -> {
//            try {
//                final val fixTimeLoadFlows = flowContext.loadFlows();
//                if (fixTimeLoadFlows != null && !fixTimeLoadFlows.isEmpty()) {
//                    fixTimeLoadFlows.forEach(defaultFlow -> {
//                        ExecutorUtil.getAsyncTaskExecutor().submit(() -> {
//                            try {
//                                defaultFlow.start();
//                            } catch (Exception e) {
//                                log.error("Exception::", e);
//                            }
//                        });
//                    });
//                }
//            } catch (Exception e) {
//                log.error("Exception::", e);
//            }
//        }, 20, 30, TimeUnit.SECONDS);
    }


}
