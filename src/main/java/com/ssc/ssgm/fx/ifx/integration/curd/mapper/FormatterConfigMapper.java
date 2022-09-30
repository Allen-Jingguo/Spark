package com.ssc.ssgm.fx.ifx.integration.curd.mapper;

import com.ssc.ssgm.fx.ifx.integration.curd.model.FormatterConfigEntity;
import com.ssc.ssgm.fx.ifx.integration.curd.model.FormatterConfigExample;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

public interface FormatterConfigMapper {
    /**
     * @mbg.generated generated automatically, do not modify!
     */
    long countByExample(FormatterConfigExample example);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int deleteByExample(FormatterConfigExample example);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int insert(FormatterConfigEntity row);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int insertSelective(FormatterConfigEntity row);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    List<FormatterConfigEntity> selectByExampleWithBLOBs(FormatterConfigExample example, RowBounds rowBounds);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    List<FormatterConfigEntity> selectByExampleWithBLOBs(FormatterConfigExample example);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    List<FormatterConfigEntity> selectByExample(FormatterConfigExample example, RowBounds rowBounds);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    List<FormatterConfigEntity> selectByExampleWithLock(FormatterConfigExample example);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    List<FormatterConfigEntity> selectByExample(FormatterConfigExample example);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int updateByExampleSelective(@Param("row") FormatterConfigEntity row, @Param("example") FormatterConfigExample example);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int updateByExampleWithBLOBs(@Param("row") FormatterConfigEntity row, @Param("example") FormatterConfigExample example);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int updateByExample(@Param("row") FormatterConfigEntity row, @Param("example") FormatterConfigExample example);

    void deleteById(String id);
}