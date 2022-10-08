
package com.ssc.ssgm.fx.ifx.integration.ui.controller;

import com.ssc.ssgm.fx.ifx.integration.common.Response;
import com.ssc.ssgm.fx.ifx.integration.core.config.KeyMapperConfig;
import com.ssc.ssgm.fx.ifx.integration.core.flow.FlowContext;
import com.ssc.ssgm.fx.ifx.integration.core.mapper.KeyMapperEnum;
import com.ssc.ssgm.fx.ifx.integration.curd.service.KeyMapperConfigService;
import com.ssc.ssgm.fx.ifx.integration.ui.dto.MapperDTO;
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
@RequestMapping(value = "/api/mapper")
@ApiOperation("inbound api")
@Slf4j
public class MapperController {

    @Autowired
    KeyMapperConfigService keyMapperConfigService;

    @Autowired
    FlowContext flowContext;

    @ApiOperation("get_type")
    @GetMapping("/get_all_key_mapper_type")
    public Response<List<KeyValue>> getTypes() {
        List<KeyMapperEnum> typeEunmList = Arrays.asList(KeyMapperEnum.values());
        List<KeyValue> types = typeEunmList.stream().map(e -> {
            KeyValue keyValue = new KeyValue();
            keyValue.setLabel(e.name());
            keyValue.setValue(e.name());
            return keyValue;
        }).collect(Collectors.toList());
        return Response.success(types);
    }

    @ApiOperation("list")
    @PostMapping("/list")
    public Response<List<MapperDTO>> list(@RequestBody MapperDTO mapperDTO) {

        List<KeyMapperConfig> configs = keyMapperConfigService.getForList(mapperDTO);
        return Response.success(configs.stream().map(e -> {
            MapperDTO mapperDTO1 = new MapperDTO();
            mapperDTO1.setType(e.getKeyMapperType().name());
            mapperDTO1.setName(e.getName());
            mapperDTO1.setCreatedTime(e.getCreatedTime());
            mapperDTO1.setId(e.getId());
            return mapperDTO1;
        }).collect(Collectors.toList()));

    }

    @ApiOperation("disable")
    @PostMapping("/disable")
    public Response<?> disable(@RequestParam("id") String id) {

//        if (flowContext.getFlowConfigMaps().stream().anyMatch(e -> e.getKeyMapperId().equals(id)&&FlowStatus.RUNNABLE.name().equals(e.getFlowStatus()))) {
//            return Response.fail("exist runnable flow ,can't disable");
//        }
//
//        keyMapperConfigService.disableConfig(id);
//        flowContext.removeKeyMapperConfig(id);
        return Response.success();
    }

    @ApiOperation("create")
    @PostMapping("/create_new")
    public Response<?> create(@RequestBody KeyMapperConfig config) {
        String id = UUID.randomUUID().toString().replace("-", "");
        config.setId(id);
        config.setCreatedTime(new Date());
        keyMapperConfigService.addConfig(config);
        flowContext.addKeyMapperConfig(config);
        return Response.success();
    }

}
