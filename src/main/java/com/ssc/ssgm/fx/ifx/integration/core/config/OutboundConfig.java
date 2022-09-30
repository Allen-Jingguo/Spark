package com.ssc.ssgm.fx.ifx.integration.core.config;

import com.ssc.ssgm.fx.ifx.integration.core.outbound.SourceOutTypeEnum;
import lombok.Data;

@Data
public class OutboundConfig extends BaseConfig {

    SourceOutTypeEnum outboundType;

    String properties;

    String hostName;
    String userName;


}

