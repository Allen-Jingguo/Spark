
package com.ssc.ssgm.fx.ifx.integration.ui.controller;

import com.ssc.ssgm.fx.ifx.integration.common.Response;
import com.ssc.ssgm.fx.ifx.integration.core.config.OutboundConfig;
import com.ssc.ssgm.fx.ifx.integration.core.flow.FlowContext;
import com.ssc.ssgm.fx.ifx.integration.core.outbound.SourceOutTypeEnum;
import com.ssc.ssgm.fx.ifx.integration.curd.service.OutboundConfigService;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
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
    FlowContext flowContext;

    @ApiOperation("get_type")
    @GetMapping("/get_types")
    public Response<List<KeyValue>> getTypes() {
        List<SourceOutTypeEnum> typeEunmList = Arrays.asList(SourceOutTypeEnum.values());
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
    public Response<List<OutboundConfig>> list() {
        List<OutboundConfig> outboundConfigs = outboundConfigService.loadAll();
        flowContext.setOutboundConfigs(outboundConfigs);
        return Response.success(outboundConfigs);
    }

    @ApiOperation("disable")
    @PostMapping("/disable")
    public Response<?> disable(@RequestParam("id") Long id) {
        if (outboundConfigService.disableConfig(id) != 1) {
            log.error("Disable config failed. No Inbound config found with ID： {} ." ,id);
            return Response.fail();
        }
        flowContext.removeSourceOutConfig(Long.toString(id));
        return Response.success();
    }

    @ApiOperation("create")
    @PostMapping("/create")
    public Response<?> create(@RequestParam("outboundConfig") OutboundConfig outboundConfig) {
        String id = UUID.randomUUID().toString().replace("-","");
        outboundConfig.setId(id);
        if(outboundConfigService.addConfig(outboundConfig) !=1){
            log.error("Outbound config creation failed.");
            return Response.fail();
        }
        flowContext.addSourceOutConfig(outboundConfig);
        return Response.success();
    }

}
