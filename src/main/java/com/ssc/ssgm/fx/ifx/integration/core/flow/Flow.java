package com.ssc.ssgm.fx.ifx.integration.core.flow;

import com.ssc.ssgm.fx.ifx.integration.core.config.FlowConfig;

public interface Flow {

    /**
     * @FlowStatus
     *
     * Status before execution : NEW ,PAUSE
     * Status after execution : RUNNABLE
     *
     */
     void start();

    /**
     * @FlowStatus
     *
     * Status before execution : RUNNABLE
     * Status after execution : TERMINATION
     *
     */
    void stop();

    /**
     * @FlowStatus
     *
     * Status before execution : RUNNABLE
     * Status after execution : PAUSE
     *
     */
    void pause();

    FlowStatus getFlowStatus();

    String getId();

    FlowConfig getFlowConfig();

    void execute();
}
