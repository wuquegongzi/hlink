package com.haibao.flink.utils;

import com.haibao.flink.cache.CacheParm;
import com.haibao.flink.connectors.side.http.HttpAsyncLookupTableSource;
import com.haibao.flink.connectors.side.jdbc.JdbcAsyncLookupTableSource;
import com.haibao.flink.enums.HttpMethodTypeEnum;
import com.haibao.flink.enums.TableTypeEnum;
import com.haibao.flink.model.Ds;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.shaded.guava18.com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/*
 * @Author ml.c
 * @Description 维表 信息 构建工具
 * @Date 22:01 2020-04-11
 **/
public class SideTableBuilder {

    private Logger LOGGER = LoggerFactory.getLogger(SideTableBuilder.class);

    /**
     * JdbcAsyncLookupTableSource
     * @param ds
     * @param type
     * @return
     */
    public JdbcAsyncLookupTableSource getJdbcAsyncLookupTableSource(Ds ds, String type){

        TranslationUtils translationUtils = new TranslationUtils();
        Map sideTableMap = translationUtils.getSideTableInfo(ds);

        Map fieldMap = (Map)sideTableMap.get("fieldMap");
        String[] fieldNames = (String[]) sideTableMap.get("fieldNames");
        TypeInformation<?>[] typeInformations = (TypeInformation<?>[])sideTableMap.get("typeInformations");
        List<String> connectionFields = (List<String>)sideTableMap.get("connectionFields");
        CacheParm caffeineCache = (CacheParm)sideTableMap.get("caffeineCache");

        LOGGER.info("缓存配置：{}",GsonUtils.gsonString(caffeineCache));

        Map clientConfig = Maps.newHashMap();
        clientConfig.put("url", fieldMap.get("connector_url"));
        clientConfig.put("user", fieldMap.get("connector_username"));
        clientConfig.put("password", fieldMap.get("connector_password"));
        clientConfig.put("max_pool_size",fieldMap.get("max_pool_size"));
        clientConfig.put("initial_pool_size",fieldMap.get("initial_pool_size"));
        clientConfig.put("min_pool_size",fieldMap.get("min_pool_size"));
        clientConfig.put("acquire_retry_attempts",fieldMap.get("acquire_retry_attempts"));
        clientConfig.put("acquire_retry_delay",fieldMap.get("acquire_retry_delay"));

        if(TableTypeEnum.SIDE_MYSQL.getType().equals(type)){
            clientConfig.put("driver_class", "com.mysql.cj.jdbc.Driver");
        }else if(TableTypeEnum.SIDE_ORACLE.getType().equals(type)){
            clientConfig.put("driver_class", "oracle.jdbc.driver.OracleDriver");
        }else{
            throw new RuntimeException("不支持的数据库类型:"+type);
        }

        JdbcAsyncLookupTableSource tableSource = JdbcAsyncLookupTableSource.Builder.newBuilder()
                .withConfigMap(clientConfig)
                .withTableName((String)fieldMap.get("table_name"))
                .withFieldNames(fieldNames)
                .withFieldTypes(typeInformations)
                //join 连接key
                .withConnectionField(connectionFields.stream().toArray(String[]::new))
                .withCacheParm(caffeineCache)
                //列层级投影 用于封装嵌套数据
                .withColumnLevelProjection(ds.getDsSchemaColumnList())
                .build();

        return tableSource;
    }


    /**
     * HttpAsyncLookupTableSource
     * @param ds
     * @return
     */
    public HttpAsyncLookupTableSource getHttpAsyncLookupTableSource(Ds ds){

        TranslationUtils translationUtils = new TranslationUtils();
        Map sideTableMap = translationUtils.getSideTableInfo(ds);

        Map fieldMap = (Map)sideTableMap.get("fieldMap");
        String[] fieldNames = (String[]) sideTableMap.get("fieldNames");
        TypeInformation<?>[] typeInformations = (TypeInformation<?>[])sideTableMap.get("typeInformations");
        List<String> connectionFields = (List<String>)sideTableMap.get("connectionFields");
        CacheParm caffeineCache = (CacheParm)sideTableMap.get("caffeineCache");

        //HTTP维表
        HttpAsyncLookupTableSource tableSource = HttpAsyncLookupTableSource.Builder.newBuilder()
                .withTableName(ds.getTableName())
                .withFieldNames(fieldNames)
                .withFieldTypes(typeInformations)
                .withMethodType(HttpMethodTypeEnum.GET)
                .withRequestUrl(String.valueOf(fieldMap.get("requestUrl")))
                .withExtraParam(null)
                .withTimeOut(Integer.valueOf((String)fieldMap.get("timeOut")))
                .withCharSet(String.valueOf(fieldMap.get("charSet")))
                .withConnectionField(connectionFields.stream().toArray(String[]::new))
                //额外参数              .withExtraParam()
                //请求类型
                 .withMedimType(null)
                //请求方式              .withMethodType()
                .withCacheParm(caffeineCache)
                //列层级投影 用于封装嵌套数据
                .withColumnLevelProjection(ds.getDsSchemaColumnList())
                .build();

        return tableSource;
    }
}
