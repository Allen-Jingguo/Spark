package com.ssc.ssgm.fx.ifx.integration.core.config;

import lombok.Data;

@Data
public class FlowConfig extends BaseConfig {

    String inboundConfigId;
    String parserType;
    String keyMapperId;
    String formatterId;
    String outboundConfigId;

    String transactionType;
    String flowStatus;
    String flowType;

}
