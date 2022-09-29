
package com.ssc.ssgm.fx.ifx.integration.ui.controller;

import com.ssc.ssgm.fx.ifx.integration.api.HackthonContext;
import com.ssc.ssgm.fx.ifx.integration.common.Response;
import com.ssc.ssgm.fx.ifx.integration.core.config.InboundConfig;
import com.ssc.ssgm.fx.ifx.integration.core.flow.FlowContext;
import com.ssc.ssgm.fx.ifx.integration.curd.service.InboundConfigService;
import com.ssc.ssgm.fx.ifx.integration.util.JsonUtil;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import com.ssc.ssgm.fx.ifx.integration.core.inbound.SourceInTypeEnum;

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
            keyValue.setName(e.toString());
            return keyValue;
        } ).collect(Collectors.toList());
        return Response.success(types);
    }

    @ApiOperation("list")
    @GetMapping("/list")
    public Response<List<InboundConfig>> list() {
        List<InboundConfig> inboundConfigList = inboundConfigService.loadAll();
        flowContext.setInboundConfigs(inboundConfigList);
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
    @PostMapping("/create")
    public Response<?> create(@RequestParam("file") MultipartFile file,
                              @RequestParam("inboundType") String inboundType,
                              HttpServletRequest request) {
        InboundConfig config = new InboundConfig();
        config.setName(file.getName());
        config.setInboundType(SourceInTypeEnum.valueOf(inboundType));
        try {
            config.setProperties(JsonUtil.byteToJson(file.getBytes()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(inboundConfigService.addConfig(config) !=1){
            log.error("Inbound config creation failed.");
            return Response.fail();
        }
        HackthonContext.inboundMap.put(config.getName(), config);
        return Response.success();
    }

}
