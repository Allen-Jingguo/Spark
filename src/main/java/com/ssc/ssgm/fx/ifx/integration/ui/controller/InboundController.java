
package com.ssc.ssgm.fx.ifx.integration.ui.controller;

import com.ssc.ssgm.fx.ifx.integration.common.Response;
import com.ssc.ssgm.fx.ifx.integration.core.config.InboundConfig;
import com.ssc.ssgm.fx.ifx.integration.core.flow.FlowContext;
import com.ssc.ssgm.fx.ifx.integration.core.inbound.SourceInTypeEnum;
import com.ssc.ssgm.fx.ifx.integration.curd.service.InboundConfigService;
import com.ssc.ssgm.fx.ifx.integration.util.KeyValueConfigLoadUtil;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/inbound")
@ApiOperation("inbound api")
@Slf4j
public class InboundController {

    @Autowired
    InboundConfigService inboundConfigService;

    @Autowired
    FlowContext flowContext;

    @ApiOperation("get_type")
    @GetMapping("/get_types")
    public Response<List<KeyValue>> getTypes() {
        List<SourceInTypeEnum> typeEunmList = Arrays.asList(SourceInTypeEnum.values());
        List<KeyValue> types = typeEunmList.stream().map(e ->{
            KeyValue keyValue = new KeyValue();
            keyValue.setLabel(e.toString());
            keyValue.setValue(e.toString());
            return keyValue;
        } ).collect(Collectors.toList());
        return Response.success(types);
    }

    @ApiOperation("list")
    @GetMapping("/list")
    public Response<List<InboundConfig>> list() {
        List<InboundConfig> inboundConfigList = inboundConfigService.loadAll();
        //flowContext.setInboundConfigs(inboundConfigList);

        inboundConfigList.forEach(e->{
            Map<String, String> map = KeyValueConfigLoadUtil.loadConfig(e.getProperties());
            e.setHostName("NA");
            e.setUserName("NA");
            if(map.get("HostName")!=null){
                e.setHostName(map.get("HostName"));
            }
            if(map.get("UserName")!=null){
                e.setUserName(map.get("HostName"));
            }
        });

        return Response.success(inboundConfigList);
    }

    @ApiOperation("disable")
    @PostMapping("/disable")
    public Response<?> disable(@RequestParam("id") Long id) {
        if (inboundConfigService.disableConfig(id) != 1) {
            log.error("Disable config failed. No Inbound config found with ID {} .",id);
            return Response.fail();
        }
        flowContext.removeSourceInConfig(Long.toString(id));
        return Response.success();
    }

    @ApiOperation("create")
    @PostMapping("/create_new")
    public Response<?> create(@RequestBody InboundConfig inboundConfig) {
        String id = UUID.randomUUID().toString().replace("-","");
        inboundConfig.setId(id);
        inboundConfig.setCreatedTime(new Date());
        String properties = inboundConfig.getProperties();
        if (StringUtils.isNoneBlank(properties)) {
            Map<String, String> stringMap = KeyValueConfigLoadUtil.loadConfig(properties);
            if (stringMap.isEmpty()) {
                return Response.fail("properties format is not correct ! please check");
            }
            properties+="\n";
            if (inboundConfig.getInboundType() == SourceInTypeEnum.JDBC) {
                properties += "driver=oracle.jdbc.driver.OracleDriver\n";
                properties += "sql=" + inboundConfig.getExecuteSql() + "\n";
                properties += "period=" + inboundConfig.getTimer() + "\n";
                inboundConfig.setProperties(properties);
            }
            //TODO

        }

        if(inboundConfigService.addConfig(inboundConfig) !=1){
            log.error("Inbound config creation failed.");
            return Response.fail();
        }
        flowContext.addSourceInConfig(inboundConfig);
        return Response.success();
    }

}
