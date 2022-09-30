package com.ssc.ssgm.fx.ifx.integration.core.flow;

import com.ssc.ssgm.fx.ifx.integration.common.exception.SystemException;
import com.ssc.ssgm.fx.ifx.integration.core.formatter.Formatter;
import com.ssc.ssgm.fx.ifx.integration.core.inbound.Inbound;
import com.ssc.ssgm.fx.ifx.integration.core.mapper.KeyMapper;
import com.ssc.ssgm.fx.ifx.integration.core.outbound.OutBound;
import com.ssc.ssgm.fx.ifx.integration.core.parser.Parser;
import com.ssc.ssgm.fx.ifx.integration.core.transformer.Transformer;
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
    FLowExecuteStatus fLowExecuteStatus;

    boolean pauseFlag = false;

    @Override
    public void execute() {
        log.info("=== Flow is executing,id={} flowName={},flowStatus={}", id, name, flowStatus.name());
        //TODO update flow status to running
        if (transActionType == FlowTransActionType.NO) {
            try {
                byte[] bytes;
                while (!pauseFlag && (bytes = inbound.next()) != null) {
                    log.info("=== Flow inside loop execution begin ,id={} flowName={},flowStatus={}", id, name, fLowExecuteStatus.name());
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
        } else {
            while (inbound.hasNext()) {
                try {
                    while (inbound.hasNext() && !pauseFlag) {
                        byte[] bytes = inbound.getWithTransaction();
                        if (bytes == null || bytes.length == 0) {
                            continue;
                        }
                        List<Map<String, Object>> dataList = parser.parse(bytes);
                        log.info("===  parser result={}", dataList);
                        for (Map<String, Object> originMap : dataList) {

                            Map<String, Object> data = originMap;
                            if (keyMapper != null) {
                                data = keyMapper.keyMap(data);
                                log.info("===  keyMapper result={}", data);
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
                            log.info("===  transform result={}", data);
                            val format = formatter.format(data);
                            log.info("===  format result={}", format);
                            outBound.putWithTransAction(format);
                        }
                    }
                } catch (Exception e) {
                    inbound.rollback();
                    outBound.rollback();
                    log.error("Exception::", e);
                } finally {
                    inbound.commit();
                    outBound.commit();
                }
            }
        }
    }

    @Override
    public void start() {

        if (this.flowStatus == FlowStatus.NEW || this.fLowExecuteStatus == FLowExecuteStatus.PAUSING) {
            this.fLowExecuteStatus = FLowExecuteStatus.RUNNING;
            this.execute();
        }
        throw new SystemException("FLowExecuteStatus error,flowStatus= " + this.flowStatus + "  flowExecuteStatus=" + this.fLowExecuteStatus);
    }

    @Override
    public void stop() {
        if (this.fLowExecuteStatus != FLowExecuteStatus.RUNNING) {
            throw new SystemException("FLowExecuteStatus is not RUNNING ");
        }

        this.fLowExecuteStatus = FLowExecuteStatus.STOPPING;
        inbound.close();
        outBound.close();
        this.fLowExecuteStatus = FLowExecuteStatus.TERMINATION;

    }

    @Override
    public void pause() {

        if (this.fLowExecuteStatus != FLowExecuteStatus.RUNNING) {
            throw new SystemException("FLowExecuteStatus is not RUNNING ");
        }

        this.pauseFlag = true;
        this.fLowExecuteStatus = FLowExecuteStatus.PAUSING;


    }

    @Override
    public FLowExecuteStatus getExecuteStatus() {
        return this.fLowExecuteStatus;
    }

    @Override
    public FlowStatus getPersistStatus() {
        return flowStatus;
    }

}


