
package com.ssc.ssgm.fx.ifx.integration.ui.controller;

import com.ssc.ssgm.fx.ifx.integration.common.Response;
import com.ssc.ssgm.fx.ifx.integration.core.config.OutboundConfig;
import com.ssc.ssgm.fx.ifx.integration.core.flow.FlowManager;
import com.ssc.ssgm.fx.ifx.integration.core.outbound.SourceOutTypeEnum;
import com.ssc.ssgm.fx.ifx.integration.curd.service.OutboundConfigService;
import com.ssc.ssgm.fx.ifx.integration.util.KeyValueConfigLoadUtil;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/outbound")
@ApiOperation("outbound api")
@Slf4j
public class OutboundController {

    @Autowired
    OutboundConfigService outboundConfigService;

    @Autowired
    FlowManager flowManager;

    @ApiOperation("get_type")
    @GetMapping("/get_types")
    public Response<List<KeyValue>> getTypes() {
        List<SourceOutTypeEnum> typeEunmList = Arrays.asList(SourceOutTypeEnum.values());
        List<KeyValue> types = typeEunmList.stream().map(e -> {
            KeyValue keyValue = new KeyValue();
            keyValue.setLabel(e.toString());
            keyValue.setValue(e.toString());
            return keyValue;
        }).collect(Collectors.toList());
        return Response.success(types);
    }

    @ApiOperation("list")
    @GetMapping("/list")
    public Response<List<OutboundConfig>> list() {

        List<OutboundConfig> outboundConfigs = new ArrayList<>(flowManager.getOutboundConfigMaps().values());
        outboundConfigs.forEach(e -> {
            Map<String, String> map = KeyValueConfigLoadUtil.loadConfig(e.getProperties());
            e.setHostName("NA");
            e.setUserName("NA");
            if (map.get("HostName") != null) {
                e.setHostName(map.get("HostName"));
            }
            if (map.get("UserName") != null) {
                e.setUserName(map.get("HostName"));
            }
        });

        //flowContext.addOutboundConfigMaps(outboundConfigs);
        return Response.success(outboundConfigs);
    }

    @ApiOperation("disable")
    @PostMapping("/disable")
    public Response<?> disable(@RequestBody OutboundConfig outboundConfig) {
        //        if (outboundConfigService.disableConfig(Long.valueOf(outboundConfig.getId())) != 1) {
        //            log.error("Disable config failed. No Inbound config found with ID： {} .", outboundConfig.getId());
        //            return Response.fail();
        //        }
        //        flowContext.removeSourceOutConfig(outboundConfig.getId());
        return Response.success();
    }

    @ApiOperation("create")
    @PostMapping("/create_new")
    public Response<?> create(@RequestBody OutboundConfig outboundConfig) {
        String id = UUID.randomUUID().toString().replace("-", "");
        outboundConfig.setId(id);
        outboundConfig.setCreatedTime(new Date());

        String properties = outboundConfig.getProperties();

        if (StringUtils.isNoneBlank(properties)) {
            //TODO
            properties+="\n";
            if (outboundConfig.getOutboundType() == SourceOutTypeEnum.JDBC) {
                //prop += "driver=oracle.jdbc.driver.OracleDriver\n";
                properties += "user=" + outboundConfig.getUserName() + "\n";
                properties += "password=" + outboundConfig.getPassword() + "\n";
                outboundConfig.setProperties(properties);
            }
        }
        if (outboundConfigService.addConfig(outboundConfig) != 1) {
            log.error("Outbound config creation failed.");
            return Response.fail();
        }
        flowManager.addSourceOutConfig(outboundConfig);
        return Response.success();
    }

}

