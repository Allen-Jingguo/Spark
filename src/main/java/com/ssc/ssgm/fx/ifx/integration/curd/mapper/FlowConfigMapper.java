package com.ssc.ssgm.fx.ifx.integration.curd.mapper;

import com.ssc.ssgm.fx.ifx.integration.curd.model.FlowConfigEntity;
import com.ssc.ssgm.fx.ifx.integration.curd.model.FlowConfigExample;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

public interface FlowConfigMapper {
    /**
     * @mbg.generated generated automatically, do not modify!
     */
    long countByExample(FlowConfigExample example);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int deleteByExample(FlowConfigExample example);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int deleteById(String id);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int insert(FlowConfigEntity row);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int insertSelective(FlowConfigEntity row);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    List<FlowConfigEntity> selectByExampleWithBLOBs(FlowConfigExample example, RowBounds rowBounds);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    List<FlowConfigEntity> selectByExampleWithBLOBs(FlowConfigExample example);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    List<FlowConfigEntity> selectByExample(FlowConfigExample example, RowBounds rowBounds);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    List<FlowConfigEntity> selectByExampleWithLock(FlowConfigExample example);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    List<FlowConfigEntity> selectByExample(FlowConfigExample example);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int updateByExampleSelective(@Param("row") FlowConfigEntity row, @Param("example") FlowConfigExample example);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int updateByExampleWithBLOBs(@Param("row") FlowConfigEntity row, @Param("example") FlowConfigExample example);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int updateByExample(@Param("row") FlowConfigEntity row, @Param("example") FlowConfigExample example);

    int updateFlowStatus(@Param("flowId")String flowId, @Param("oldStatus")String oldStatus,@Param("expectStatus") String expectStatus);

}