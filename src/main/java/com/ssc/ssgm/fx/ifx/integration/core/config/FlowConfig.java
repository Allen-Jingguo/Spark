package com.ssc.ssgm.fx.ifx.integration.core.config;

import lombok.Data;

import java.util.Date;

@Data
public class FlowConfig extends BaseConfig {


    String id;
    String name;
    Date createdTime;

    String inboundName;
    String parserName;
    String filterName;
    String keyMapperName;
    String outboundName;



}
