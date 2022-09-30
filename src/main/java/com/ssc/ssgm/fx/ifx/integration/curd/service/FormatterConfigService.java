package com.ssc.ssgm.fx.ifx.integration.curd.service;

import com.ssc.ssgm.fx.ifx.integration.core.config.FormatterConfig;
import com.ssc.ssgm.fx.ifx.integration.core.formatter.FormatterEnum;
import com.ssc.ssgm.fx.ifx.integration.curd.mapper.FormatterConfigMapper;
import com.ssc.ssgm.fx.ifx.integration.curd.model.FormatterConfigEntity;
import com.ssc.ssgm.fx.ifx.integration.curd.model.FormatterConfigExample;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FormatterConfigService {

    @Autowired
    FormatterConfigMapper formatterConfigMapper;

    public List<FormatterConfig> loadAll() {

        FormatterConfigExample formatterConfigExample = new FormatterConfigExample();
        final List<FormatterConfigEntity> flowConfigs = formatterConfigMapper.selectByExampleWithBLOBs(formatterConfigExample);
        final List<FormatterConfig> collect = flowConfigs.stream().map(e -> {
            FormatterConfig config = new FormatterConfig();
            BeanUtils.copyProperties(e, config);
            config.setFormatterType(FormatterEnum.valueOf(e.getFormatterType()));
            return config;
        }).collect(Collectors.toList());

        return collect;
    }

    public void addConfig(FormatterConfig formatterConfig) {

        FormatterConfigEntity entity = new FormatterConfigEntity();
        BeanUtils.copyProperties(formatterConfig, entity);
        entity.setFormatterType(formatterConfig.getFormatterType().name());
        formatterConfigMapper.insert(entity);
    }

    public void disableConfig(String id) {
        formatterConfigMapper.deleteById(id);
    }
}
