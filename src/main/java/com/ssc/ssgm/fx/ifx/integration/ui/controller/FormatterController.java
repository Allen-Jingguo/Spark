
package com.ssc.ssgm.fx.ifx.integration.ui.controller;

import com.ssc.ssgm.fx.ifx.integration.common.Response;
import com.ssc.ssgm.fx.ifx.integration.core.config.FormatterConfig;
import com.ssc.ssgm.fx.ifx.integration.core.flow.FlowContext;
import com.ssc.ssgm.fx.ifx.integration.core.flow.FlowStatus;
import com.ssc.ssgm.fx.ifx.integration.core.formatter.FormatterEnum;
import com.ssc.ssgm.fx.ifx.integration.curd.service.FormatterConfigService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
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
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/formatter")
@ApiOperation("inbound api")
@Slf4j
public class FormatterController {

    @Autowired
    FormatterConfigService formatterConfigService;

    @Autowired
    FlowContext flowContext;

    @ApiOperation("get_all_formatter_type")
    @GetMapping("/get_all_formatter_type")
    public Response<List<KeyValue>> getTypes() {
        List<FormatterEnum> typeEunmList = Arrays.asList(FormatterEnum.values());
        List<KeyValue> types = typeEunmList.stream().map(e -> {
            KeyValue keyValue = new KeyValue();
            keyValue.setLabel(e.name());
            keyValue.setName(e.name());
            return keyValue;
        }).collect(Collectors.toList());
        return Response.success(types);
    }

    @ApiOperation("list")
    @GetMapping("/list")
    public Response<List<FormatterConfig>> list(@RequestBody FormatterConfig formatterConfig) {
        //TODO to be optimized
        List<FormatterConfig> configs = formatterConfigService.loadAll();
        List<FormatterConfig> collect = configs.stream().filter(e -> {
            if (formatterConfig.getName() != null && !formatterConfig.getName().equals(e.getName())) {
                return false;
            }
            if (formatterConfig.getFormatterType() != null && !formatterConfig.getFormatterType().equals(e.getFormatterType())) {
                return false;
            }
            return true;
        }).collect(Collectors.toList());
        return Response.success(collect);
    }

    @ApiOperation("disable")
    @PostMapping("/disable")
    public Response<?> disable(@RequestParam("id") String id) {

        if (flowContext.getFlowConfigs().stream().anyMatch(e -> e.getFormatterId().equals(id) && FlowStatus.RUNNABLE.name().equals(e.getFlowStatus()))) {
            return Response.fail("exist runnable flow ,can't disable");
        }

        formatterConfigService.disableConfig(id);
        flowContext.removeFormatterConfig(id);

        return Response.success();
    }

    @ApiOperation("create")
    @PostMapping("/create_new")
    public Response<?> create(@RequestBody FormatterConfig config) {
        String id = UUID.randomUUID().toString().replace("-", "");
        config.setId(id);
        config.setCreatedTime(new Date());
        formatterConfigService.addConfig(config);
        flowContext.addFormatterConfig(config);
        return Response.success();
    }

}
