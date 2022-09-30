package com.ssc.ssgm.fx.ifx.integration.curd.service;

import com.ssc.ssgm.fx.ifx.integration.core.config.FlowConfig;
import com.ssc.ssgm.fx.ifx.integration.core.flow.FlowStatus;
import com.ssc.ssgm.fx.ifx.integration.curd.mapper.FlowConfigMapper;
import com.ssc.ssgm.fx.ifx.integration.curd.model.FlowConfigEntity;
import com.ssc.ssgm.fx.ifx.integration.curd.model.FlowConfigExample;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AutoConfigureAfter(FlowConfigMapper.class)
public class FlowConfigService {

    @Autowired
    FlowConfigMapper flowConfigMapper;

    public List<FlowConfig> loadAll() {

        FlowConfigExample flowConfigExample = new FlowConfigExample();
        final List<FlowConfigEntity> flowConfigs = flowConfigMapper.selectByExampleWithBLOBs(flowConfigExample);
        final List<FlowConfig> collect = flowConfigs.stream().map(e -> {
            FlowConfig config = new FlowConfig();
            BeanUtils.copyProperties(e, config);
            return config;
        }).collect(Collectors.toList());

        return collect;
    }

    public int addConfig(FlowConfig flowConfig) {
        FlowConfigEntity entity = new FlowConfigEntity();
        BeanUtils.copyProperties(flowConfig, entity);
        return flowConfigMapper.insert(entity);
    }

    public int disableConfig(Long id) {
        return flowConfigMapper.deleteById(Long.toString(id));
    }


    public void updateFlowStatusByName(String name,String status){
        FlowConfigEntity flowConfigEntity = new FlowConfigEntity();
        flowConfigEntity.setFlowStatus(status);
        FlowConfigExample example = new FlowConfigExample();
        FlowConfigExample.Criteria criteria = example.createCriteria();
        criteria.andNameEqualTo(name);
        flowConfigMapper.updateByExampleSelective(flowConfigEntity,example);
    }
}
