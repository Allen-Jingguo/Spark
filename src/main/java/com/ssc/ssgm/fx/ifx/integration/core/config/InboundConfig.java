package com.ssc.ssgm.fx.ifx.integration.core.config;

import com.ssc.ssgm.fx.ifx.integration.core.inbound.SourceInTypeEnum;
import lombok.Data;

@Data
public class InboundConfig extends BaseConfig {

    SourceInTypeEnum inboundType;

    String properties;

    String hostName;
    String userName;
    String executeSql;
    String timer;

//    String driver;
//    String url;
//    String user;
//    String password;


}

