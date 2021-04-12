package com.haibao.flink.connectors.side.http;

import com.haibao.flink.cache.AbstractSideCache;
import com.haibao.flink.cache.CacheParm;
import com.haibao.flink.cache.CacheTypeEnum;
import com.haibao.flink.cache.W_TinyLFUSideCache;
import com.haibao.flink.enums.HttpMedimTypeEnum;
import com.haibao.flink.enums.HttpMethodTypeEnum;
import com.haibao.flink.enums.SideConstants;
import com.haibao.flink.model.DsSchemaColumn;
import com.haibao.flink.utils.GsonUtils;
import com.haibao.flink.utils.TranslationUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.typeutils.RowTypeInfo;
import org.apache.flink.shaded.guava18.com.google.common.collect.Lists;
import org.apache.flink.table.functions.AsyncTableFunction;
import org.apache.flink.table.functions.FunctionContext;
import org.apache.flink.table.functions.FunctionRequirement;
import org.apache.flink.types.Row;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletableFuture;

/**
*  @author: c.zh
*  @description: 构建HttpAsyncLookupFunction，使用CloseableHttpAsyncClient异步加载数据
*  @date: 2020/3/5
**/
public class HttpAsyncLookupFunction extends AsyncTableFunction<Row> {

    private Logger LOGGER = LoggerFactory.getLogger(HttpAsyncLookupFunction.class);

    private final String[] fieldNames;
    private final    TypeInformation[] fieldTypes;
    private String   requestUrl;
    private HttpMethodTypeEnum methodType;
    private Map<String,String>   extraParam;
    private Integer  timeOut;
    private String   charSet;
    private String[] connectionField;
    private HttpMedimTypeEnum medimType;
    private String  tableName;

    private CacheParm cacheParm;
    private AbstractSideCache sideCache;

    private List<DsSchemaColumn> dsSchemaColumnList;

    public HttpAsyncLookupFunction(Builder builder) {

        this.requestUrl = builder.requestUrl;
        this.fieldNames = builder.fieldNames;
        this.fieldTypes = builder.fieldTypes;
        this.methodType = builder.methodType;
        this.extraParam=builder.extraParam;
        this.timeOut=builder.timeOut;
        this.charSet=builder.charSet;
        this.connectionField=builder.connectionField;
        this.medimType=builder.medimType;
        this.tableName=builder.tableName;
        this.cacheParm = builder.cacheParm;
        this.dsSchemaColumnList = builder.dsSchemaColumnList;
    }

    /**
     *   根据传递的keys异步查询维表数据
     * @param resultFuture
     * @param keys   源表某些字段的值，通常用来做数据筛选时使用
     */
    public void eval(CompletableFuture<Collection<Row>> resultFuture, Object... keys) {

        String cacheKey = tableName.concat("_").concat(StringUtils.join(keys, "-"));

        //http 异步回调
        FutureCallback<HttpResponse> callback = new FutureCallback<HttpResponse>() {
            @Override
            public void completed(HttpResponse result) {
                try {
                    HttpEntity entity = result.getEntity();
                    String  record = EntityUtils.toString(entity,charSet);
                    //关闭输入流
                    EntityUtils.consume(entity);
                    List<Row> rowList = Lists.newArrayList();
                    if(record!=null){
                        //无法支持 json数据嵌套，废弃
//                        Map<String,Object> resMap= GsonUtils.GsonToMaps(str);
//                        rowList.add(buildRow(resMap));

                        Row row;
                        try {
                            row = convertToRow(record);
                            rowList.add(row);
                            resultFuture.complete(rowList);
                        } catch (IOException e) {
                            LOGGER.error("数据转换填充异常，反序列化失败，忽略！异常信息：{}",e.getMessage());
                            resultFuture.complete(Collections.emptyList());
                        }

                    }else {
                        resultFuture.complete(Collections.emptyList());
                    }

                    //使用缓存,为空默认也放缓存，防止缓存穿透
                    if(openCacheBoolean()) {
                        sideCache.putCache(cacheKey,rowList);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                }
            }
            @Override
            public void failed(Exception e) {
                LOGGER.error(e.getMessage());
            }
            @Override
            public void cancelled() {
                LOGGER.info("http请求取消");
            }
        };

        try {
            List<Row> rowList;
            //使用缓存
            if(openCacheBoolean()) {
                rowList= (List<Row>)sideCache.getFromCache(cacheKey);
                if(null != rowList){
                    resultFuture.complete(rowList);
                    return;
                }
            }

            Map<String,String> hashMap=new HashMap<>(16);
            if(extraParam!=null) {
                hashMap.putAll(extraParam);
            }
            for(int i=0;i<connectionField.length;i++){
                hashMap.put(connectionField[i],(String) keys[i]);
            }
            //todo 根据请求参数添加本地缓存,key值定义规则需要确定
            if (SideConstants.METHOD_GET.equals(methodType.name())) {
                HttpAsyUtils.execute(requestUrl, hashMap, timeOut, callback, methodType.name(), charSet, medimType.getType());
            } else {
                if (SideConstants.MEDIMTYPE_JSON.equals(medimType.getType())) {
                    String jsonParam = GsonUtils.gsonString(hashMap);
                    HttpAsyUtils.execute(requestUrl, jsonParam, timeOut, callback, methodType.name(), charSet, medimType.getType());
                } else if (SideConstants.MEDIMTYPE_URLENCODED.equals(medimType.getType())) {
                    HttpAsyUtils.execute(requestUrl, hashMap, timeOut, callback, methodType.name(), charSet, medimType.getType());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * @param record
     * @return
     * @throws IOException
     */
    public Row convertToRow(String record) throws IOException {

        if(null == dsSchemaColumnList){
            return buildRow(GsonUtils.gsonToMaps(record));
        }else{
            TranslationUtils translationUtils = new TranslationUtils();
            return translationUtils.convertToRow(record,fieldNames,fieldTypes,dsSchemaColumnList,"0");
        }
     }


    /**
     * 保留 平铺数据,主要用于测试
     * @param map
     * @return
     */
    private Row buildRow(Map<String,Object> map) {
        Row row = new Row(fieldNames.length);
        for (int i = 0; i < fieldNames.length; i++) {
            row.setField(i, map.get(fieldNames[i]));
        }
        return row;
    }

    /**
     * 数据返回类型
     * @return
     */
    @Override
    public TypeInformation<Row> getResultType() {
        return  new RowTypeInfo(fieldTypes, fieldNames);
    }

    @Override
    public void open(FunctionContext context) throws Exception {

        initCache();
    }

    /**
     * 缓存初始化
     */
    private void initCache(){
        AbstractSideCache sideCache;

        if(openCacheBoolean()){
            //默认LRU
            if(CacheTypeEnum.W_TinyLFU.getCacheType().equals(cacheParm.getCacheType())){
                sideCache = new W_TinyLFUSideCache(cacheParm);
                this.sideCache = sideCache;

            }else{
                throw new RuntimeException("not support side cache with type:" + cacheParm.getCacheType());
            }
            sideCache.initCache();
        }
    }

    private boolean openCacheBoolean(){
        return !CacheTypeEnum.NONE.getCacheType().equals(cacheParm.getCacheType());
    }


    @Override
    public void close() throws Exception {
        super.close();
    }

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public Set<FunctionRequirement> getRequirements() {
        return null;
    }

    @Override
    public boolean isDeterministic() {
        return false;
    }

    public static final class Builder {

        private String[] fieldNames;
        private TypeInformation[] fieldTypes;
        private String requestUrl;
        private HttpMethodTypeEnum methodType;
        private Map<String,String>   extraParam;
        private Integer timeOut;
        private String  charSet;
        private String[] connectionField;
        private HttpMedimTypeEnum medimType;
        private String  tableName;

        private CacheParm cacheParm;

        private List<DsSchemaColumn> dsSchemaColumnList;

        private Builder() {
        }

        public static Builder getBuilder() {
            return new Builder();
        }

        public Builder withFieldNames(String[] fieldNames) {
            this.fieldNames = fieldNames;
            return this;
        }

        public Builder withFieldTypes(TypeInformation[] fieldTypes) {
            this.fieldTypes = fieldTypes;
            return this;
        }

        public Builder withRequestUrl(String requestUrl){
            this.requestUrl = requestUrl;
            return this;
        }
        public Builder withMethodType(HttpMethodTypeEnum methodType) {
            this.methodType = methodType;
            return this;
        }
        public Builder withExetParam(Map<String,String> extraParam) {
            this.extraParam = extraParam;
            return this;
        }
        public Builder withTimeOut(Integer timeOut) {
            this.timeOut = timeOut;
            return this;
        }
        public Builder withCharSet(String charSet) {
            this.charSet = charSet;
            return this;
        }
        public Builder withConnectionField(String[] connectionField) {
            this.connectionField = connectionField;
            return this;
        }
        public Builder withMedimType(HttpMedimTypeEnum medimType) {
            this.medimType = medimType;
            return this;
        }
        public Builder withTableName(String tableName) {
            this.tableName = tableName;
            return this;
        }
        public Builder withCacheParm(CacheParm cacheParm) {
            this.cacheParm = cacheParm;
            return this;
        }

        public Builder withColumnLevelProjection(List<DsSchemaColumn> dsSchemaColumnList) {
            this.dsSchemaColumnList = dsSchemaColumnList;
            return this;
        }

        public HttpAsyncLookupFunction build() {
            return new HttpAsyncLookupFunction(this);
        }
    }


}
