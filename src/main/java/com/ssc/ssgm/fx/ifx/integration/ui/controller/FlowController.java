
package com.ssc.ssgm.fx.ifx.integration.ui.controller;

import com.ssc.ssgm.fx.ifx.integration.common.Response;
import com.ssc.ssgm.fx.ifx.integration.core.config.FlowConfig;
import com.ssc.ssgm.fx.ifx.integration.core.config.FormatterConfig;
import com.ssc.ssgm.fx.ifx.integration.core.config.InboundConfig;
import com.ssc.ssgm.fx.ifx.integration.core.config.KeyMapperConfig;
import com.ssc.ssgm.fx.ifx.integration.core.config.OutboundConfig;
import com.ssc.ssgm.fx.ifx.integration.core.flow.Flow;
import com.ssc.ssgm.fx.ifx.integration.core.flow.FlowContext;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
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
        List<InboundConfig> inboundConfigList = new ArrayList<>(flowContext.getInboundConfigMaps().values());
        List<KeyValue> inbounds = inboundConfigList.stream().map(e -> {
            KeyValue tKeyValue = new KeyValue();
            tKeyValue.setLabel(e.getName());
            tKeyValue.setValue(e.getId());
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
        List<KeyMapperConfig> keyMapperConfigs = new ArrayList<>(flowContext.getKeyMapperConfigMaps().values());
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
        List<FormatterConfig> formatterConfigs = new ArrayList<>(flowContext.getFormatterConfigMaps().values());
        List<KeyValue> keyValues = formatterConfigs.stream().map(e -> {
            KeyValue keyValue = new KeyValue();
            keyValue.setValue(e.getId());
            keyValue.setLabel(e.getName());
            return keyValue;
        }).collect(Collectors.toList());
        return Response.success(keyValues);
    }


    @ApiOperation("get_outbounds")
    @GetMapping("/get_outbounds")
    public Response<List<KeyValue>> getOutbounds() {
        List<OutboundConfig> outboundConfigList = new ArrayList<>(flowContext.getOutboundConfigMaps().values());
        List<KeyValue> outbounds = outboundConfigList.stream().map(e -> {
            KeyValue tKeyValue = new KeyValue();
            tKeyValue.setLabel(e.getName());
            tKeyValue.setValue(e.getId());
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

        String executeStatus;
    }

    @ApiOperation("list")
    @GetMapping("/list")
    public Response<List<FlowListDTO>> list() {

        //flowContext.initLoadFlows();

        List<Flow> flows = new ArrayList<>(flowContext.getFlowsMap().values());
        List<FlowListDTO> collect = flows.stream().map(flow -> {
            FlowConfig config = flow.getFlowConfig();

            FlowListDTO dto = new FlowListDTO();
            BeanUtils.copyProperties(config, dto);
            KeyMapperConfig mapperConfig = flowContext.getKeyMapperConfigMaps().get(config.getKeyMapperId());
            FormatterConfig formatterConfig = flowContext.getFormatterConfigMaps().get(config.getFormatterId());
            InboundConfig inboundConfig = flowContext.getInboundConfigMaps().get(config.getInboundConfigId());
            OutboundConfig outboundConfig = flowContext.getOutboundConfigMaps().get(config.getOutboundConfigId());

            if (mapperConfig != null) {
                dto.setKeyMapperName(mapperConfig.getName());
            }
            if (formatterConfig != null) {
                dto.setFormatterName(formatterConfig.getName());
            }
            if (inboundConfig != null) {
                dto.setInboundName(inboundConfig.getName());
            }
            if (mapperConfig != null) {
                dto.setOutboundName(outboundConfig.getName());
            }
            dto.setParserName(config.getParserType());
            dto.setExecuteStatus(config.getFlowStatus());
            return dto;
        }).collect(Collectors.toList());
        return Response.success(collect);
    }

    @ApiOperation("disable")
    @GetMapping("/disable")
    public Response<?> disable(@RequestBody FlowConfig config) {

        Flow flow = flowContext.getFlow(config.getId());
        if (flow != null) {
            try {
                flow.stop();
            } catch (Exception e) {
                log.error("Exception::", e);
                return Response.fail("stop fail ");
            }
        }


        if (flowConfigService.disableConfig(Long.valueOf(config.getId())) != 1) {
            log.error("Disable config failed. No FLow config found with ID {} .", config.getId());
            return Response.fail();
        }

        return Response.success();
    }

    @ApiOperation("create")
    @PostMapping("/create_new")
    public Response<?> create(@RequestBody FlowConfig flowConfig) {
        flowContext.addFlow(flowConfig);
        return Response.success();
    }

    @ApiOperation("start")
    @PostMapping("/start")
    public Response<?> start(@RequestBody FlowDTO flowDTO) {

        Flow flow = flowContext.getFlow(flowDTO.getId());
        if (flow != null) {
            try {
                flow.start();
            } catch (Exception e) {
                log.error("Exception::", e);
                return Response.fail("start fail ");
            }
            return Response.success();
        }
        return Response.fail("start fail ");
    }

    @ApiOperation("pause")
    @PostMapping("/pause")
    public Response<?> pause(@RequestBody FlowDTO flowDTO) {

        Flow flow = flowContext.getFlow(flowDTO.getId());
        if (flow != null) {
            try {
                flow.pause();
            } catch (Exception e) {
                log.error("Exception::", e);
                return Response.fail("pause fail ");
            }
            return Response.success();
        }
        return Response.fail("pause fail ");

    }

    @ApiOperation("stop")
    @PostMapping("/stop")
    public Response<?> stop(@RequestBody FlowDTO flowDTO) {

        Flow flow = flowContext.getFlow(flowDTO.getId());
        if (flow != null) {
            try {
                flow.stop();
            } catch (Exception e) {
                log.error("Exception::", e);
                return Response.fail("stop fail ");
            }
            return Response.success();
        }
        return Response.fail("stop fail ");
    }

}
