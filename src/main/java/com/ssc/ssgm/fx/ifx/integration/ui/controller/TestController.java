
package com.ssc.ssgm.fx.ifx.integration.ui.controller;

import com.ssc.ssgm.fx.ifx.integration.common.Response;
import com.ssc.ssgm.fx.ifx.integration.core.config.*;
import com.ssc.ssgm.fx.ifx.integration.core.flow.FlowStatus;
import com.ssc.ssgm.fx.ifx.integration.core.flow.FlowTransActionType;
import com.ssc.ssgm.fx.ifx.integration.core.formatter.FormatterEnum;
import com.ssc.ssgm.fx.ifx.integration.core.inbound.SourceInTypeEnum;
import com.ssc.ssgm.fx.ifx.integration.core.mapper.KeyMapperEnum;
import com.ssc.ssgm.fx.ifx.integration.core.outbound.SourceOutTypeEnum;
import com.ssc.ssgm.fx.ifx.integration.core.parser.ParserEnum;
import com.ssc.ssgm.fx.ifx.integration.curd.service.*;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/flow")
@ApiOperation("flow api")
@Slf4j
public class TestController {

    @Autowired
    FlowConfigService flowConfigService;

    @Autowired
    InboundConfigService inboundConfigService;

    @Autowired
    OutboundConfigService outboundConfigService;

    @Autowired
    FormatterConfigService formatterConfigService;

    @Autowired
    KeyMapperConfigService keyMapperConfigService;

    @ApiOperation("test")
    @GetMapping("/test")
    public Response<?> test() {

        String id = UUID.randomUUID().toString().replaceAll("-", "");

        InboundConfig inboundConfig = new InboundConfig();
        String p1 = "driver=oracle.jdbc.driver.OracleDriver\n" +
                "url=jdbc:oracle:thin:@//10.1.126.224:1528/O03FFO0\n" +
                "user=sdrrpt\n" +
                "password=sdrrpt\n" +
                "sql=select name from INBOUND_CONFIG\n" +
                "period=30\n";
        inboundConfig.setProperties(p1);
        inboundConfig.setInboundType(SourceInTypeEnum.JDBC);
        inboundConfig.setCreatedTime(new Date());
        inboundConfig.setId(id);
        inboundConfig.setName("test");
        inboundConfigService.addConfig(inboundConfig);

        String p2 = "driver=oracle.jdbc.driver.OracleDriver\n" +
                "url=jdbc:oracle:thin:@//10.1.126.224:1528/O03FFO0\n" +
                "user=sdrrpt\n" +
                "password=sdrrpt\n";
        OutboundConfig outboundConfig = new OutboundConfig();
        outboundConfig.setProperties(p2);
        outboundConfig.setOutboundType(SourceOutTypeEnum.JDBC);
        outboundConfig.setCreatedTime(new Date());
        outboundConfig.setId(id);
        outboundConfig.setName("test");
        outboundConfigService.addConfig(outboundConfig);

        FormatterConfig formatterConfig = new FormatterConfig();
        String p3 = "INSERT INTO TEST_YECJ (NAME)  VALUES('${name2}');\n";
        formatterConfig.setProperties(null);
        formatterConfig.setTemplate(p3);
        formatterConfig.setFormatterType(FormatterEnum.JDBC);
        formatterConfig.setCreatedTime(new Date());
        formatterConfig.setId(id);
        formatterConfig.setName("test");
        formatterConfigService.addConfig(formatterConfig);

        KeyMapperConfig keyMapperConfig = new KeyMapperConfig();
        String p4 = "NAME=name2\n";
        keyMapperConfig.setProperties(p4);
        keyMapperConfig.setKeyMapperType(KeyMapperEnum.KEY_MAPPER);
        keyMapperConfig.setCreatedTime(new Date());
        keyMapperConfig.setId(id);
        keyMapperConfig.setName("test");
        keyMapperConfigService.addConfig(keyMapperConfig);

        FlowConfig flowConfig = new FlowConfig();
        flowConfig.setCreatedTime(new Date());
        flowConfig.setId(id);
        flowConfig.setName("test");

        flowConfig.setFlowType("default");
        flowConfig.setInboundConfigId(id);
        flowConfig.setOutboundConfigId(id);
        flowConfig.setKeyMapperId(id);
        flowConfig.setFormatterId(id);
        flowConfig.setParserType(ParserEnum.KEY_VALUE.name());

        flowConfig.setFlowStatus(FlowStatus.RUNNABLE.name());
        flowConfig.setTransactionType(FlowTransActionType.NO.name());
        flowConfigService.addConfig(flowConfig);

        return Response.success();
    }


}
