package com.ssc.ssgm.fx.ifx.integration.core.flow;

import com.ssc.ssgm.fx.ifx.integration.common.exception.SystemException;
import com.ssc.ssgm.fx.ifx.integration.core.config.FlowConfig;
import com.ssc.ssgm.fx.ifx.integration.core.formatter.Formatter;
import com.ssc.ssgm.fx.ifx.integration.core.inbound.Inbound;
import com.ssc.ssgm.fx.ifx.integration.core.mapper.KeyMapper;
import com.ssc.ssgm.fx.ifx.integration.core.outbound.OutBound;
import com.ssc.ssgm.fx.ifx.integration.core.parser.Parser;
import com.ssc.ssgm.fx.ifx.integration.core.parser.ParserEnum;
import com.ssc.ssgm.fx.ifx.integration.core.transformer.Transformer;
import com.ssc.ssgm.fx.ifx.integration.util.ExecutorUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

import java.util.List;
import java.util.Map;

/**
 * InBound->Parser->Mapper->transformer->formatter-OutBound
 * <p>
 * flow status
 * <p>
 * start
 * pause
 * stop
 *
 * <p>
 * DefaultFlow
 * TestController
 * AppTaskExecutor
 * <p>
 * 1、登环境,安装工具 all
 * 2、前端搭建起来，写个大致design(chunjing)，（有时间写页面） ，3个页面
 * 3、能用什么 aws service,怎么用，有时间实现h2 (jingguo)
 * <p>
 * 4、JDBCInbound,JDBCOutbound(已经实现,  如果不能使用s3, 使用H2 ），
 * 再写一个kafkaInbound, kafkaOutbound ( tang,bao)
 */
@Data
@AllArgsConstructor
@Builder
@Slf4j
public class DefaultFlow implements Flow {

    String id;
    String name;

    Inbound inbound;
    Parser parser;
    KeyMapper keyMapper;
    List<Transformer> transformers;
    Formatter formatter;
    OutBound outBound;

    FlowStatus flowStatus;
    FlowTransActionType transActionType;

    FlowContext flowContext;

    FlowConfig flowConfig;

    boolean pauseFlag = false;
    boolean runFlag = false;


    public void executeInner() {
        log.info("=== Flow is executing,id={} flowName={},flowStatus={}", id, name, flowStatus.name());
        //TODO TransAction
        if (transActionType == FlowTransActionType.NO) {

        } else {

        }

        try {
            byte[] bytes;
            while (!pauseFlag && (bytes = inbound.next()) != null) {
                log.info("=== Flow inside loop execution begin ,id={} flowName={},flowStatus={}", id, name, flowStatus.name());
                if (bytes.length == 0) {
                    log.info("== inbound next data is empty !");
                    continue;
                }
                List<Map<String, Object>> dataList = parser.parse(bytes);
                log.info("===  parser result={}", dataList);
                for (Map<String, Object> originMap : dataList) {

                    Map<String, Object> data = originMap;
                    if (keyMapper != null) {
                        data = keyMapper.keyMap(data);
                        log.info("=== keyMapper result={}", data);
                    }
                    for (Transformer transformer : transformers) {
                        try {
                            if (!transformer.transform(data)) {
                                break;
                            }
                        } catch (Exception e) {
                            log.error("Exception::", e);
                        }
                    }
                    log.info("=== transform result={}", data);
                    val format = formatter.format(data);
                    log.info("=== format result={}", format);
                    outBound.put(format);
                    log.info("=== Flow inside loop execution end ");
                }
            }
        } catch (Exception e) {
            log.error("Exception::", e);
        }
    }

    private void init() {
        val inboundConfig = this.flowContext.getInboundConfigMaps().get(flowConfig.getInboundConfigId());
        val outBoundConfig = this.flowContext.getOutboundConfigMaps().get(flowConfig.getOutboundConfigId());
        val mapperConfig = this.flowContext.getKeyMapperConfigMaps().get(flowConfig.getKeyMapperId());
        val formatterConfig = this.flowContext.getFormatterConfigMaps().get(flowConfig.getFormatterId());

        this.inbound = Inbound.build(inboundConfig);
        this.parser = ParserEnum.getParser(flowConfig.getParserType());
        this.keyMapper = KeyMapper.build(mapperConfig);
        this.formatter = Formatter.build(formatterConfig);
        this.outBound = OutBound.build(outBoundConfig);
    }

    @Override
    public void execute() {
        if (this.flowStatus == FlowStatus.RUNNABLE) {
            ExecutorUtil.getAsyncTaskExecutor().submit(() -> {
                try {
                    this.init();
                    this.executeInner();
                } catch (Exception e) {
                    log.error("Exception::", e);
                }
            });
            return ;
        }
        throw new SystemException("FLowExecuteStatus error,flowStatus= " + this.flowStatus);
    }

    @Override
    public synchronized void start() {

        if (this.flowStatus == FlowStatus.NEW || this.flowStatus == FlowStatus.PAUSE) {
            flowContext.updateFlowStatus(this.getId(), this.flowStatus, FlowStatus.RUNNABLE);
            this.flowStatus = FlowStatus.RUNNABLE;
            ExecutorUtil.getAsyncTaskExecutor().submit(() -> {
                try {
                    this.init();
                    this.executeInner();
                } catch (Exception e) {
                    log.error("Exception::", e);
                }
            });
            return ;
        }
        throw new SystemException("FLowExecuteStatus error,flowStatus= " + this.flowStatus);

    }

    @Override
    public synchronized void stop() {
        if (this.flowStatus == FlowStatus.RUNNABLE || this.flowStatus == FlowStatus.PAUSE) {
            flowContext.updateFlowStatus(this.getId(), this.flowStatus, FlowStatus.STOPPING);
            this.flowStatus = FlowStatus.STOPPING;
            ExecutorUtil.getAsyncTaskExecutor().submit(() -> {
                try {
                    try {
                        this.pauseFlag = true;
                        inbound.close();
                        outBound.close();
                        flowContext.updateFlowStatus(this.getId(), this.flowStatus, FlowStatus.TERMINATION);
                        this.flowStatus = FlowStatus.TERMINATION;
                    } catch (Exception e) {
                        log.error("Exception::", e);
                    }
                } catch (Exception e) {
                    log.error("Exception::", e);
                }
            });
            return ;
        }
        throw new SystemException("flowStatus error,flowStatus= " + this.flowStatus);
    }

    @Override
    public synchronized void pause() {

        if (this.flowStatus == FlowStatus.RUNNABLE) {
            flowContext.updateFlowStatus(this.getId(), this.flowStatus, FlowStatus.PAUSING);
            this.flowStatus = FlowStatus.PAUSING;
            ExecutorUtil.getAsyncTaskExecutor().submit(() -> {
                try {
                    this.pauseFlag = true;
                    inbound.close();
                    outBound.close();
                    flowContext.updateFlowStatus(this.getId(), this.flowStatus, FlowStatus.PAUSE);
                    this.flowStatus = FlowStatus.PAUSE;
                } catch (Exception e) {
                    log.error("Exception::", e);
                }
            });
            return ;
        }
        throw new SystemException("flowStatus error,flowStatus= " + this.flowStatus);
    }

    @Override
    public FlowStatus getFlowStatus() {
        return flowStatus;
    }

}


