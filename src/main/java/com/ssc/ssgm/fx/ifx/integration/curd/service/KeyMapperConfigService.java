package com.ssc.ssgm.fx.ifx.integration.curd.service;

import com.ssc.ssgm.fx.ifx.integration.core.config.KeyMapperConfig;
import com.ssc.ssgm.fx.ifx.integration.core.mapper.KeyMapperEnum;
import com.ssc.ssgm.fx.ifx.integration.curd.mapper.KeyMapperConfigMapper;
import com.ssc.ssgm.fx.ifx.integration.curd.model.KeyMapperConfigEntity;
import com.ssc.ssgm.fx.ifx.integration.curd.model.KeyMapperConfigExample;
import com.ssc.ssgm.fx.ifx.integration.ui.dto.MapperDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class KeyMapperConfigService {

    @Autowired
    KeyMapperConfigMapper keyMapperConfigMapper;

    public List<KeyMapperConfig> loadAll() {
        KeyMapperConfigExample inboundConfigExample = new KeyMapperConfigExample();
        final List<KeyMapperConfigEntity> flowConfigs = keyMapperConfigMapper.selectByExampleWithBLOBs(inboundConfigExample);
        final List<KeyMapperConfig> collect = flowConfigs.stream().map(e -> {
            KeyMapperConfig config = new KeyMapperConfig();
            BeanUtils.copyProperties(e, config);
            config.setKeyMapperType(KeyMapperEnum.valueOf(e.getKeyMapperType()));
            return config;
        }).collect(Collectors.toList());

        return collect;
    }

    public void addConfig(KeyMapperConfig keyMapperConfig) {

        KeyMapperConfigEntity entity = new KeyMapperConfigEntity();
        BeanUtils.copyProperties(keyMapperConfig, entity);
        entity.setKeyMapperType(keyMapperConfig.getKeyMapperType().name());
        keyMapperConfigMapper.insert(entity);

    }

    public List<KeyMapperConfig> getForList(MapperDTO mapperDTO) {

        KeyMapperConfigExample inboundConfigExample = new KeyMapperConfigExample();
        KeyMapperConfigExample.Criteria criteria = inboundConfigExample.createCriteria();
        if(mapperDTO.getType() != null){
            criteria.andNameEqualTo(mapperDTO.getName().trim());
        }

        if (mapperDTO.getType() != null) {
            criteria.andKeyMapperTypeEqualTo(mapperDTO.getType());
        }
        final List<KeyMapperConfigEntity> flowConfigs = keyMapperConfigMapper.selectByExampleWithBLOBs(inboundConfigExample);
        final List<KeyMapperConfig> collect = flowConfigs.stream().map(e -> {
            KeyMapperConfig config = new KeyMapperConfig();
            BeanUtils.copyProperties(e, config);
            config.setKeyMapperType(KeyMapperEnum.valueOf(e.getKeyMapperType()));
            return config;
        }).collect(Collectors.toList());
        return collect;

    }

    public void disableConfig(String id) {
        keyMapperConfigMapper.deleteById(id);
    }
}
