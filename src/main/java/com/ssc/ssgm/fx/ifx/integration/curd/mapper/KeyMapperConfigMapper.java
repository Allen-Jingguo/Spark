package com.ssc.ssgm.fx.ifx.integration.curd.mapper;

import com.ssc.ssgm.fx.ifx.integration.curd.model.KeyMapperConfigEntity;
import com.ssc.ssgm.fx.ifx.integration.curd.model.KeyMapperConfigExample;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

public interface KeyMapperConfigMapper {
    /**
     * @mbg.generated generated automatically, do not modify!
     */
    long countByExample(KeyMapperConfigExample example);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int deleteByExample(KeyMapperConfigExample example);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int insert(KeyMapperConfigEntity row);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int insertSelective(KeyMapperConfigEntity row);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    List<KeyMapperConfigEntity> selectByExampleWithBLOBs(KeyMapperConfigExample example, RowBounds rowBounds);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    List<KeyMapperConfigEntity> selectByExampleWithBLOBs(KeyMapperConfigExample example);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    List<KeyMapperConfigEntity> selectByExample(KeyMapperConfigExample example, RowBounds rowBounds);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    List<KeyMapperConfigEntity> selectByExampleWithLock(KeyMapperConfigExample example);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    List<KeyMapperConfigEntity> selectByExample(KeyMapperConfigExample example);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int updateByExampleSelective(@Param("row") KeyMapperConfigEntity row, @Param("example") KeyMapperConfigExample example);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int updateByExampleWithBLOBs(@Param("row") KeyMapperConfigEntity row, @Param("example") KeyMapperConfigExample example);

    /**
     * @mbg.generated generated automatically, do not modify!
     */
    int updateByExample(@Param("row") KeyMapperConfigEntity row, @Param("example") KeyMapperConfigExample example);

    void deleteById(String id);
}