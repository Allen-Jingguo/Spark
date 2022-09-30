
package com.ssc.ssgm.fx.ifx.integration.ui.controller;

import com.ssc.ssgm.fx.ifx.integration.common.Response;
import com.ssc.ssgm.fx.ifx.integration.core.config.FlowConfig;
import com.ssc.ssgm.fx.ifx.integration.core.config.FormatterConfig;
import com.ssc.ssgm.fx.ifx.integration.core.config.InboundConfig;
import com.ssc.ssgm.fx.ifx.integration.core.config.KeyMapperConfig;
import com.ssc.ssgm.fx.ifx.integration.core.config.OutboundConfig;
import com.ssc.ssgm.fx.ifx.integration.core.flow.FlowContext;
import com.ssc.ssgm.fx.ifx.integration.core.flow.FlowStatus;
import com.ssc.ssgm.fx.ifx.integration.core.flow.FlowTransActionType;
import com.ssc.ssgm.fx.ifx.integration.core.parser.ParserEnum;
import com.ssc.ssgm.fx.ifx.integration.curd.service.FlowConfigService;
import com.ssc.ssgm.fx.ifx.integration.ui.dto.FlowDTO;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/flow")
@ApiOperation("flow api")
@Slf4j
public class FlowController {

    @Autowired
    FlowConfigService flowConfigService;

    @Autowired
    FlowContext flowContext;

    @ApiOperation("get_flow_types")
    @GetMapping("/get_flow_types")
    public Response<List<KeyValue>> getFlowTypes() {
        List<FlowTransActionType> typeEunmList = Arrays.asList(FlowTransActionType.values());
        List<KeyValue> typeList = typeEunmList.stream().map(e -> {
            KeyValue keyValue = new KeyValue();
            keyValue.setLabel(e.toString());
            keyValue.setValue(e.toString());
            return keyValue;
        }).collect(Collectors.toList());
        return Response.success(typeList);
    }

    @ApiOperation("get_inbounds")
    @GetMapping("/get_inbounds")
    public Response<List<KeyValue>> getInbounds() {
        List<InboundConfig> inboundConfigList = flowContext.getInboundConfigs();
        List<KeyValue> inbounds = inboundConfigList.stream().map(e -> {
            KeyValue tKeyValue = new KeyValue();
            tKeyValue.setLabel(e.getName());
            tKeyValue.setValue(e.getName());
            return tKeyValue;
        }).collect(Collectors.toList());
        return Response.success(inbounds);
    }

    @ApiOperation("get_parsers")
    @GetMapping("/get_parsers")
    public Response<List<KeyValue>> getParsers() {
        List<ParserEnum> typeEunmList = Arrays.asList(ParserEnum.values());
        List<KeyValue> typeList = typeEunmList.stream().map(e -> {
            KeyValue keyValue = new KeyValue();
            keyValue.setValue(e.toString());
            keyValue.setLabel(e.toString());
            return keyValue;
        }).collect(Collectors.toList());
        return Response.success(typeList);
    }

    @ApiOperation("get_key_mappers")
    @GetMapping("/get_key_mappers")
    public Response<List<KeyValue>> get_key_mappers() {
        List<KeyMapperConfig> keyMapperConfigs = flowContext.getKeyMapperConfigs();
        List<KeyValue> keyValues = keyMapperConfigs.stream().map(e -> {
            KeyValue keyValue = new KeyValue();
            keyValue.setValue(e.getName());
            keyValue.setLabel(e.getName());
            return keyValue;
        }).collect(Collectors.toList());
        return Response.success(keyValues);
    }

    @ApiOperation("get_formatters")
    @GetMapping("/get_formatters")
    public Response<List<KeyValue>> get_formatters() {
        List<FormatterConfig> formatterConfigs = flowContext.getFormatterConfigs();
        List<KeyValue> keyValues = formatterConfigs.stream().map(e -> {
            KeyValue keyValue = new KeyValue();
            keyValue.setValue(e.getName());
            keyValue.setLabel(e.getName());
            return keyValue;
        }).collect(Collectors.toList());
        return Response.success(keyValues);
    }


    @ApiOperation("get_outbounds")
    @GetMapping("/get_outbounds")
    public Response<List<KeyValue>> getOutbounds() {
        List<OutboundConfig> outboundConfigList = flowContext.getOutboundConfigs();
        List<KeyValue> outbounds = outboundConfigList.stream().map(e -> {
            KeyValue tKeyValue = new KeyValue();
            tKeyValue.setLabel(e.getName());
            tKeyValue.setValue(e.getName());
            return tKeyValue;
        }).collect(Collectors.toList());
        return Response.success(outbounds);
    }

    @Data
    class FlowListDTO {

        String id;
        String name;
        Date createdTime;

        String inboundName;
        String parserName;
        String formatterName;
        String keyMapperName;
        String outboundName;
    }

    @ApiOperation("list")
    @GetMapping("/list")
    public Response<List<FlowListDTO>> list() {
        flowContext.loadFlows();
        List<FlowConfig> flowConfigs = flowContext.getFlowConfigs();
        List<FlowListDTO> collect = flowConfigs.stream().map(e -> {
            FlowListDTO dto = new FlowListDTO();
            BeanUtils.copyProperties(e, dto);
            List<KeyMapperConfig> mapperConfigs = flowContext.getKeyMapperConfigs().stream()
                    .filter(v -> v.getId().equals(e.getKeyMapperId())).collect(Collectors.toList());

            List<FormatterConfig> formatterConfigs = flowContext.getFormatterConfigs().stream()
                    .filter(v -> v.getId().equals(e.getFormatterId())).collect(Collectors.toList());
            List<InboundConfig> inboundConfigs = flowContext.getInboundConfigs().stream()
                    .filter(v -> v.getId().equals(e.getInboundConfigId())).collect(Collectors.toList());
            List<OutboundConfig> outboundConfigs = flowContext.getOutboundConfigs().stream()
                    .filter(v -> v.getId().equals(e.getOutboundConfigId())).collect(Collectors.toList());

            if (!mapperConfigs.isEmpty()) {
                dto.setKeyMapperName(mapperConfigs.get(0).getId());
            }
            if (!formatterConfigs.isEmpty()) {
                dto.setFormatterName(formatterConfigs.get(0).getId());
            }
            if (!inboundConfigs.isEmpty()) {
                dto.setInboundName(inboundConfigs.get(0).getId());
            }
            if (!mapperConfigs.isEmpty()) {
                dto.setOutboundName(outboundConfigs.get(0).getId());
            }
            return dto;
        }).collect(Collectors.toList());
        return Response.success(collect);
    }

    @ApiOperation("disable")
    @GetMapping("/disable")
    public Response<?> disable(@RequestBody FlowConfig config) {
        if (flowConfigService.disableConfig(Long.valueOf(config.getId())) != 1) {
            log.error("Disable config failed. No FLow config found with ID {} .", config.getId());
            return Response.fail();
        }
        flowContext.removeFlowConfig(config.getId());
        return Response.success();
    }

    @ApiOperation("create")
    @PostMapping("/create_new")
    public Response<?> create(@RequestBody FlowConfig flowConfig) {
        String id = UUID.randomUUID().toString().replace("-", "");
        flowConfig.setId(id);
        flowConfig.setCreatedTime(new Date());
        if (flowConfigService.addConfig(flowConfig) != 1) {
            return Response.fail();
        }
        flowContext.addFlowConfig(flowConfig);
        return Response.success();
    }

    @ApiOperation("start")
    @PostMapping("/start")
    public Response<?> start(@RequestBody FlowDTO flowDTO) {
        flowConfigService.updateFlowStatusByName(flowDTO.getName(), FlowStatus.NEW.name());
        return Response.success();
    }

    @ApiOperation("pause")
    @PostMapping("/pause")
    public Response<?> pause(@RequestBody FlowDTO flowDTO) {
        flowConfigService.updateFlowStatusByName(flowDTO.getName(), FlowStatus.PAUSE.name());
        return Response.success();
    }

    @ApiOperation("stop")
    @PostMapping("/stop")
    public Response<?> stop(@RequestBody FlowDTO flowDTO) {
        flowConfigService.updateFlowStatusByName(flowDTO.getName(), FlowStatus.TERMINATION.name());
        return Response.success();
    }

}
