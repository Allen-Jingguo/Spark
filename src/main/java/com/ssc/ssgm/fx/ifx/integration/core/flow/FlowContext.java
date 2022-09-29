package com.ssc.ssgm.fx.ifx.integration.core.flow;

import com.ssc.ssgm.fx.ifx.integration.core.config.*;
import com.ssc.ssgm.fx.ifx.integration.core.formatter.Formatter;
import com.ssc.ssgm.fx.ifx.integration.core.inbound.Inbound;
import com.ssc.ssgm.fx.ifx.integration.core.mapper.KeyMapper;
import com.ssc.ssgm.fx.ifx.integration.core.outbound.OutBound;
import com.ssc.ssgm.fx.ifx.integration.core.parser.ParserEnum;
import com.ssc.ssgm.fx.ifx.integration.core.transformer.Transformer;
import com.ssc.ssgm.fx.ifx.integration.curd.service.*;
import lombok.Data;
import lombok.val;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

@Service
@Data
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


    List<FlowConfig> flowConfigs = new CopyOnWriteArrayList<>();

    List<FormatterConfig> formatterConfigs = new CopyOnWriteArrayList<>();

    List<KeyMapperConfig> keyMapperConfigs = new CopyOnWriteArrayList<>();

    List<InboundConfig> inboundConfigs = new CopyOnWriteArrayList<>();

    List<OutboundConfig> outboundConfigs = new CopyOnWriteArrayList<>();

    List<Flow> flows = new CopyOnWriteArrayList<>();

    public void addFlowConfig(FlowConfig flowConfig) {
        flowConfigs.add(flowConfig);
    }

    public void addFormatterConfig(FormatterConfig formatterConfig) {
        formatterConfigs.add(formatterConfig);
    }

    public void addKeyMapperConfig(KeyMapperConfig keyMapperConfig) {
        keyMapperConfigs.add(keyMapperConfig);
    }

    public void addSourceInConfig(InboundConfig inboundConfig) {
        inboundConfigs.add(inboundConfig);
    }

    public void addSourceOutConfig(OutboundConfig outboundConfig) {
        outboundConfigs.add(outboundConfig);
    }

    public void removeFlowConfig(String id) { flowConfigs.removeIf(flowConfig->{ return flowConfig.getId().equals(id);});}

    public void removeFormatterConfig(String id) { formatterConfigs.removeIf(formatterConfig ->{ return formatterConfig.getId().equals(id);});}

    public void removeKeyMapperConfig(String id) { keyMapperConfigs.removeIf(keyMapperConfig->{ return keyMapperConfig.getId().equals(id);});}

    public void removeSourceInConfig(String id) { inboundConfigs.removeIf(inboundConfig->{ return inboundConfig.getId().equals(id);});}

    public void removeSourceOutConfig(String id) { outboundConfigs.removeIf(outboundConfig->{ return outboundConfig.getId().equals(id);});}
    public List<Flow> loadFlows() {

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
        // get flow
        List<Flow> defaultFlowList = flowConfigs.entrySet().stream().map(e -> {
            FlowConfig flowConfig = e.getValue();
            val inboundConfig = sourceInConfigs.get(flowConfig.getInboundConfigId());
            val outBoundConfig = sourceOutConfigs.get(flowConfig.getOutboundConfigId());
            val mapperConfig = keyMapperConfigs.get(flowConfig.getKeyMapperId());
            val formatterConfig = formatterConfigs.get(flowConfig.getFormatterId());

            Map<String, Transformer> transformerMap = ac.getBeansOfType(Transformer.class);

            val flow = DefaultFlow.builder().inbound(Inbound.build(inboundConfig))
                    .parser(ParserEnum.getParser(flowConfig.getParserType()))
                    .keyMapper(KeyMapper.build(mapperConfig))
                    .transformers(new ArrayList<>(transformerMap.values()))
                    .formatter(Formatter.build(formatterConfig))
                    .outBound(OutBound.build(outBoundConfig))
                    .transActionType(FlowTransActionType.valueOf(flowConfig.getTransactionType()))
                    .flowStatus(FlowStatus.valueOf(flowConfig.getFlowStatus()))
                    .id(flowConfig.getId())
                    .name(flowConfig.getName())
                    .build();
            return flow;
        }).collect(Collectors.toList());


        //filter loaded flow
        List<Flow> filteredFlows = defaultFlowList.stream().filter(e -> {
            return e.getPersistStatus() == FlowStatus.RUNNABLE || !flows.stream().anyMatch(e1 -> e.getId().equals(e1.getId()));
        }).collect(Collectors.toList());

        flows.addAll(filteredFlows);

        return filteredFlows;

    }

    private Map<String, InboundConfig> getSourceInConfigs() {
        List<InboundConfig> configs = this.inboundConfigService.loadAll();
        Map<String, InboundConfig> map = configs.stream().collect(Collectors.toMap(e -> e.getId(), e -> e));
        return map;
    }

    private Map<String, FlowConfig> getAllFlowConfigs() {
        List<FlowConfig> configs = this.flowConfigService.loadAll();
        Map<String, FlowConfig> map = configs.stream().collect(Collectors.toMap(e -> e.getId(), e -> e));
        return map;
    }

    private Map<String, KeyMapperConfig> getFlowKeyMapperConfigs() {
        List<KeyMapperConfig> configs = this.keyMapperConfigService.loadAll();
        Map<String, KeyMapperConfig> map = configs.stream().collect(Collectors.toMap(e -> e.getId(), e -> e));
        return map;
    }

    private Map<String, OutboundConfig> getSourceOutConfigs() {
        List<OutboundConfig> configs = this.outboundConfigService.loadAll();
        Map<String, OutboundConfig> map = configs.stream().collect(Collectors.toMap(e -> e.getId(), e -> e));
        return map;
    }

    private Map<String, FormatterConfig> getFlowFormatterConfigs() {
        List<FormatterConfig> configs = this.formatterConfigService.loadAll();
        Map<String, FormatterConfig> map = configs.stream().collect(Collectors.toMap(e -> e.getId(), e -> e));
        return map;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.ac = applicationContext;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }

}
