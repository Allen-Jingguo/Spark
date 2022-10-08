package com.ssc.ssgm.fx.ifx.integration.core.flow;

public interface Flow {

    void start();

    void stop();

    void pause();

    void execute();

    FLowExecuteStatus getExecuteStatus();

    FlowStatus getPersistStatus();

    String getId();

}
