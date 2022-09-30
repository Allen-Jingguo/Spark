package com.ssc.ssgm.fx.ifx.integration.core.config;

import com.ssc.ssgm.fx.ifx.integration.core.formatter.FormatterEnum;
import lombok.Data;

@Data
public class FormatterConfig extends BaseConfig{


    String template;

    @Deprecated
    String properties;

    FormatterEnum formatterType;

}
