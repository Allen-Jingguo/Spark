package com.ssc.ssgm.fx.ifx.integration.core.flow;

import com.ssc.ssgm.fx.ifx.integration.common.exception.SystemException;
import com.ssc.ssgm.fx.ifx.integration.core.config.FlowConfig;
import com.ssc.ssgm.fx.ifx.integration.core.config.FormatterConfig;
import com.ssc.ssgm.fx.ifx.integration.core.config.InboundConfig;
import com.ssc.ssgm.fx.ifx.integration.core.config.KeyMapperConfig;
import com.ssc.ssgm.fx.ifx.integration.core.config.OutboundConfig;
import com.ssc.ssgm.fx.ifx.integration.core.transformer.Transformer;
import com.ssc.ssgm.fx.ifx.integration.curd.service.FlowConfigService;
import com.ssc.ssgm.fx.ifx.integration.curd.service.FormatterConfigService;
import com.ssc.ssgm.fx.ifx.integration.curd.service.InboundConfigService;
import com.ssc.ssgm.fx.ifx.integration.curd.service.KeyMapperConfigService;
import com.ssc.ssgm.fx.ifx.integration.curd.service.OutboundConfigService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Data
@Slf4j
public class FlowContext implements ApplicationContextAware, InitializingBean {

    ApplicationContext ac;

    @Autowired
    FlowConfigService flowConfigService;

    @Autowired
    InboundConfigService inboundConfigService;

    @Autowired
    OutboundConfigService outboundConfigService;

    @Autowired
    FormatterConfigService formatterConfigService;

    @Autowired
    KeyMapperConfigService keyMapperConfigService;


    Map<String, FlowConfig> flowConfigMaps = new ConcurrentHashMap<>();

    Map<String, FormatterConfig> formatterConfigMaps = new ConcurrentHashMap<>();

    Map<String, KeyMapperConfig> keyMapperConfigMaps = new ConcurrentHashMap<>();

    Map<String, InboundConfig> inboundConfigMaps = new ConcurrentHashMap<>();

    Map<String, OutboundConfig> outboundConfigMaps = new ConcurrentHashMap<>();


    Map<String, Flow> flowsMap = new ConcurrentHashMap<>();

    public Flow getFlow(String flowId) {
        return flowsMap.get(flowId);
    }

    //    public Map<String, Flow> getFLowMap() {
    //        Map<String, Flow> result = new HashMap<>();
    //        flowsMap.forEach(e -> {
    //            result.put(e.getId(), e);
    //        });
    //        return result;
    //    }

    public void addFlowConfig(FlowConfig flowConfig) {
        if (this.flowConfigMaps.containsKey(flowConfig.getId())) {
            return;
        }
        flowConfigMaps.put(flowConfig.getId(), flowConfig);
    }

    public void addFlow(FlowConfig flowConfig) {

        String id = UUID.randomUUID().toString().replace("-", "");
        flowConfig.setId(id);
        flowConfig.setCreatedTime(new Date());
        flowConfig.setFlowStatus(FlowStatus.NEW.name());

        if (this.flowConfigMaps.containsKey(flowConfig.getId())) {
            return;
        }

        if (flowConfigService.addConfig(flowConfig) != 1) {
            return;
        }

        Map<String, Transformer> transformerMap = ac.getBeansOfType(Transformer.class);
        val flow = DefaultFlow.builder()
                //                        .inbound(Inbound.build(inboundConfig))
                //                        .parser(ParserEnum.getParser(flowConfig.getParserType()))
                //                        .keyMapper(KeyMapper.build(mapperConfig))
                .transformers(new ArrayList<>(transformerMap.values()))
                //                        .formatter(Formatter.build(formatterConfig))
                //                        .outBound(OutBound.build(outBoundConfig))
                .transActionType(FlowTransActionType.valueOf(flowConfig.getTransactionType()))
                .flowStatus(FlowStatus.NEW)
                .id(flowConfig.getId())
                .name(flowConfig.getName())
                .flowConfig(flowConfig)
                .flowContext(this)
                .build();

        flowConfigMaps.put(flowConfig.getId(), flowConfig);
        flowsMap.put(flowConfig.getId(), flow);

    }

    public void addFormatterConfig(FormatterConfig formatterConfig) {
        if (this.formatterConfigMaps.containsKey(formatterConfig.getId())) {
            return;
        }
        formatterConfigMaps.put(formatterConfig.getId(), formatterConfig);
    }

    public void addKeyMapperConfig(KeyMapperConfig keyMapperConfig) {
        if (this.keyMapperConfigMaps.containsKey(keyMapperConfig.getId())) {
            return;
        }
        keyMapperConfigMaps.put(keyMapperConfig.getId(), keyMapperConfig);
    }

    public void addSourceInConfig(InboundConfig inboundConfig) {
        if (this.inboundConfigMaps.containsKey(inboundConfig.getId())) {
            return;
        }
        inboundConfigMaps.put(inboundConfig.getId(), inboundConfig);
    }

    public void addSourceOutConfig(OutboundConfig outboundConfig) {
        if (this.outboundConfigMaps.containsKey(outboundConfig.getId())) {
            return;
        }
        outboundConfigMaps.put(outboundConfig.getId(), outboundConfig);
    }

//        public void removeFlowConfig(String id) {flowConfigMaps.removeIf(flowConfig -> {return flowConfig.getId().equals(id);});}
//
//        public void removeFormatterConfig(String id) {formatterConfigMaps.removeIf(formatterConfig -> {return formatterConfig.getId().equals(id);});}
//
//        public void removeKeyMapperConfig(String id) {keyMapperConfigMaps.removeIf(keyMapperConfig -> {return keyMapperConfig.getId().equals(id);});}
//
//        public void removeSourceInConfig(String id) {inboundConfigMaps.removeIf(inboundConfig -> {return inboundConfig.getId().equals(id);});}
//
//        public void removeSourceOutConfig(String id) {outboundConfigMaps.removeIf(outboundConfig -> {return outboundConfig.getId().equals(id);});}

    public void initLoadFlows() {

        // load  config
        Map<String, FlowConfig> flowConfigs = this.getAllFlowConfigs();
        Map<String, FormatterConfig> formatterConfigs = this.getFlowFormatterConfigs();
        Map<String, KeyMapperConfig> keyMapperConfigs = this.getFlowKeyMapperConfigs();
        Map<String, InboundConfig> sourceInConfigs = this.getSourceInConfigs();
        Map<String, OutboundConfig> sourceOutConfigs = this.getSourceOutConfigs();
        flowConfigs.values().forEach(e -> {
            this.addFlowConfig(e);
        });
        formatterConfigs.values().forEach(e -> {
            this.addFormatterConfig(e);
        });
        keyMapperConfigs.values().forEach(e -> {
            this.addKeyMapperConfig(e);
        });
        sourceInConfigs.values().forEach(e -> {
            this.addSourceInConfig(e);
        });
        sourceOutConfigs.values().forEach(e -> {
            this.addSourceOutConfig(e);
        });

        flowConfigs.keySet().forEach(key -> {
            if (flowsMap.containsKey(key)) {
                return;
            }

            FlowConfig flowConfig = flowConfigs.get(key);
            try {
                log.info("init load flow config value:{}", flowConfig);

                Map<String, Transformer> transformerMap = ac.getBeansOfType(Transformer.class);
                val flow = DefaultFlow.builder()
                        //                        .inbound(Inbound.build(inboundConfig))
                        //                        .parser(ParserEnum.getParser(flowConfig.getParserType()))
                        //                        .keyMapper(KeyMapper.build(mapperConfig))
                        .transformers(new ArrayList<>(transformerMap.values()))
                        //                        .formatter(Formatter.build(formatterConfig))
                        //                        .outBound(OutBound.build(outBoundConfig))
                        .transActionType(FlowTransActionType.valueOf(flowConfig.getTransactionType()))
                        .flowStatus(FlowStatus.valueOf(flowConfig.getFlowStatus()))
                        .id(flowConfig.getId())
                        .name(flowConfig.getName())
                        .flowConfig(flowConfig)
                        .flowContext(this)
                        .build();

                flowsMap.put(flowConfig.getId(), flow);
            } catch (BeansException e) {
                log.error("BeansException", e);
            } catch (IllegalArgumentException e) {
                log.error("IllegalArgumentException", e);
            }
        });

    }


    private Map<String, InboundConfig> getSourceInConfigs() {
        List<InboundConfig> configs = this.inboundConfigService.loadAll();
        //Map<String, InboundConfig> map = configs.stream().collect(Collectors.toMap(e -> e.getId(), e -> e));

        Map<String, InboundConfig> result = new HashMap<>();
        configs.forEach(e -> {
            result.put(e.getId(), e);
        });

        return result;
    }

    private Map<String, FlowConfig> getAllFlowConfigs() {
        List<FlowConfig> configs = this.flowConfigService.loadAll();
        //Map<String, FlowConfig> map = configs.stream().collect(Collectors.toMap(e -> e.getId(), e -> e));

        Map<String, FlowConfig> result = new HashMap<>();
        configs.forEach(e -> {
            result.put(e.getId(), e);
        });
        return result;
    }

    private Map<String, KeyMapperConfig> getFlowKeyMapperConfigs() {
        List<KeyMapperConfig> configs = this.keyMapperConfigService.loadAll();

        Map<String, KeyMapperConfig> result = new HashMap<>();
        configs.forEach(e -> {
            result.put(e.getId(), e);
        });

        //Map<String, KeyMapperConfig> map = configs.stream().collect(Collectors.toMap(e -> e.getId(), e -> e));
        return result;
    }

    private Map<String, OutboundConfig> getSourceOutConfigs() {
        List<OutboundConfig> configs = this.outboundConfigService.loadAll();

        Map<String, OutboundConfig> result = new HashMap<>();
        configs.forEach(e -> {
            result.put(e.getId(), e);
        });

        //Map<String, OutboundConfig> map = configs.stream().collect(Collectors.toMap(e -> e.getId(), e -> e));
        return result;
    }

    private Map<String, FormatterConfig> getFlowFormatterConfigs() {
        List<FormatterConfig> configs = this.formatterConfigService.loadAll();
        Map<String, FormatterConfig> result = new HashMap<>();
        configs.forEach(e -> {
            result.put(e.getId(), e);
        });
        //Map<String, FormatterConfig> map = configs.stream().collect(Collectors.toMap(e -> e.getId(), e -> e));
        return result;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.ac = applicationContext;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }

    public void updateFlowStatus(String flowId, FlowStatus oldStatus, FlowStatus expectStatus) {

        if (this.flowsMap.get(flowId) == null) {
            throw new SystemException("flow is null");
        }

        Flow flow = this.flowsMap.get(flowId);
        if (!flowConfigService.updateFlowStatus(flowId, oldStatus.name(), expectStatus.name())) {
            throw new SystemException("update  flow status fail , flow =" + flow.toString());
        }

    }
}
