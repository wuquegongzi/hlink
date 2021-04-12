package com.haibao.admin.web.mapper;

import com.haibao.admin.web.entity.TDsSchemaColumn;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p>
 * 数据源的schema解析后的属性 Mapper 接口
 * </p>
 *
 * @author jobob
 * @since 2020-02-25
 */
@Repository
public interface DsSchemaColumnMapper extends BaseMapper<TDsSchemaColumn> {

    int insertBatch(List<TDsSchemaColumn> tDsSchemaColumns);
}
