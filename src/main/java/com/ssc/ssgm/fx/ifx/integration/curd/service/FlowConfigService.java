package com.ssc.ssgm.fx.ifx.integration.curd.service;

import com.ssc.ssgm.fx.ifx.integration.core.config.FlowConfig;
import com.ssc.ssgm.fx.ifx.integration.core.flow.FlowContext;
import com.ssc.ssgm.fx.ifx.integration.curd.mapper.FlowConfigMapper;
import com.ssc.ssgm.fx.ifx.integration.curd.mapper.InboundConfigMapper;
import com.ssc.ssgm.fx.ifx.integration.curd.mapper.OutboundConfigMapper;
import com.ssc.ssgm.fx.ifx.integration.curd.model.FlowConfigEntity;
import com.ssc.ssgm.fx.ifx.integration.curd.model.FlowConfigExample;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AutoConfigureAfter(FlowConfigMapper.class)
public class FlowConfigService {

    @Autowired
    FlowConfigMapper flowConfigMapper;

    @Autowired
    InboundConfigMapper inboundConfigMapper;

    @Autowired
    OutboundConfigMapper outboundConfigMapper;

    @Autowired
    FlowContext flowContext;

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


    @Transactional
    public void updateFlowStatus(String name,String newStatus){

//        /**
//         * find flow
//          */
//        FlowConfigExample example = new FlowConfigExample();
//        FlowConfigExample.Criteria criteria = example.createCriteria();
//        criteria.andNameEqualTo(name);
//        criteria.andFlowStatusEqualTo(oldStatus);
//        List<FlowConfigEntity> flowConfigEntities = flowConfigMapper.selectByExample(example);
//        if (!CollectionUtils.isEmpty(flowConfigEntities)){
//            FlowConfigEntity flowConfig= flowConfigEntities.get(0);
//            List<Flow> flows = flowContext.getFlows().stream().filter(item -> item.getId() == flowConfig.getId()).collect(Collectors.toList());
//            if (!CollectionUtils.isEmpty(flows)){
//                Flow flow = flows.get(0);
//                switch (newStatus){
//                    case NEW: flow.start();
//                    case PAUSE: flow.pause();
//                    case TERMINATION: flow.stop();
//                    default:
//                }
//            }
//        }
        /**
         * update in db
         */
        updateStatusByName(name, newStatus);


    }
    @Transactional
    public void updateStatusByName(String name, String status) {
        /**
         * update flow
         */
        FlowConfigEntity flowConfigEntity = new FlowConfigEntity();
        flowConfigEntity.setFlowStatus(status);
        FlowConfigExample example = new FlowConfigExample();
        FlowConfigExample.Criteria criteria = example.createCriteria();
        criteria.andNameEqualTo(name);
        flowConfigMapper.updateByExampleSelective(flowConfigEntity,example);

    }

}
