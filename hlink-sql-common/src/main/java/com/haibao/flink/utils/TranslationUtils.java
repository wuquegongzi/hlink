package com.haibao.flink.utils;

import cn.hutool.core.util.StrUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.haibao.flink.cache.CacheParm;
import com.haibao.flink.cache.CacheTypeEnum;
import com.haibao.flink.enums.FlinkSQLTypesEnum;
import com.haibao.flink.enums.JsonSchemaTypesEnum;
import com.haibao.flink.model.Ds;
import com.haibao.flink.model.DsSchemaColumn;
import com.haibao.flink.model.JsonField;
import com.haibao.flink.model.Option;
import com.haibao.flink.model.UnionField;

import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.shaded.guava18.com.google.common.collect.Maps;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.JsonNode;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.flink.types.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/*
 * @Author ml.c
 * @Description 转换提取工具
 * @Date 22:46 2020-04-11
 **/
public class TranslationUtils {

    private Logger LOGGER = LoggerFactory.getLogger(TranslationUtils.class);

    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 转换数据为行
     * @param record  返回数据
     * @param fieldNames  列名
     * @param fieldTypes 列类型
     * @param levelList 列层级投影
     *
     * @return
     */
    public Row convertToRow(String record, String[] fieldNames, TypeInformation[] fieldTypes, List<DsSchemaColumn> levelList, String pId) throws JsonProcessingException {

        //根级
        if("0".equals(pId)){
            AtomicReference<String> virtualId = new AtomicReference<>("1");
            levelList.forEach(dsSchemaColumn -> {
                if(pId.equals(dsSchemaColumn.getVirtualPid())
                        && "Row".equals(dsSchemaColumn.getBasicType())){
                    virtualId.set(dsSchemaColumn.getVirtualId());
                }
            });

            //找一级子节点，即列属性
            return convertToRow(record,fieldNames,fieldTypes,levelList,virtualId.get());
        }else{
            JsonNode root = objectMapper.readTree(record);

            Row row = new Row(fieldNames.length);
            for (int i = 0; i < fieldNames.length; i++) {
                JsonNode node = getIgnoreCase(root, fieldNames[i]);
                if (null == node) {
                    row.setField(i, null);
                    continue;
                }

                for (DsSchemaColumn dsSchemaColumn : levelList) {
                    if(fieldNames[i].equals(dsSchemaColumn.getName())){

                        String basicType = dsSchemaColumn.getBasicType();

                        //实体类型
                        if("Row".equals(basicType) && node.isPojo()){

                            List<DsSchemaColumn> dsSchemaColumns =  dsSchemaColumnsConvertBasic(levelList, dsSchemaColumn.getVirtualId());

                            List<String> subFieldNames = new LinkedList<>();
                            List<TypeInformation> typeInformations = new LinkedList<>();

                            for (DsSchemaColumn d:dsSchemaColumns) {
                                subFieldNames.add(d.getName());
                                typeInformations.add(d.getTypeInformation());
                            }
                            String[] fieldNameArray = subFieldNames.stream().toArray(String[]::new);
                            TypeInformation[] typeInformationArry = typeInformations.stream().toArray(TypeInformation[]::new);

                            String childJson = objectMapper.writeValueAsString(node);
                            Row subRow = convertToRow(childJson,fieldNameArray,typeInformationArry,levelList,dsSchemaColumn.getVirtualId());
                            row.setField(i,subRow);
                        }
                        //数组类型
                        else if(basicType.contains("List") && node.isArray()){
                            //填充为数组,需判断不同数组类型
                            convertToArray(node,levelList,dsSchemaColumn,row,i);
                        }else{
                            // Read the value as specified type
                            Object value = objectMapper.treeToValue(node, fieldTypes[i].getTypeClass());
                            row.setField(i, value);
                        }
                    }
                }
            }

            return row;
        }
    }

    /**
     * 填充数组类数据
     * @param node
     * @param levelList
     * @param dsSchemaColumn
     * @param row
     * @param i
     * @throws JsonProcessingException
     */
    private void convertToArray(JsonNode node, List<DsSchemaColumn> levelList, DsSchemaColumn dsSchemaColumn, Row row, int i) throws JsonProcessingException {

        List<DsSchemaColumn> dsSchemaColumns =  dsSchemaColumnsConvertBasic(levelList, dsSchemaColumn.getVirtualId());

        Iterator<JsonNode> subElements = node.elements();

        TypeInformation typeInformation;
        if(dsSchemaColumns.size()>0 ){
            //默认 一个数组类嵌套，只能嵌套一个类型，默认取第一个
            typeInformation = dsSchemaColumns.get(0).getTypeInformation();
            //TypeInformation t =Types.OBJECT_ARRAY(typeInformation);

            LOGGER.info("数组嵌套类,子类型：{}",typeInformation);

            if (typeInformation == Types.STRING) {
                String[] s = new String[node.size()];
                int j =0;
                while (subElements.hasNext()){
                    s[j] = subElements.next().asText();
                    j++;
                }
                row.setField(i,s);
            }
            else if(typeInformation == Types.INT){
                int[] s = new int[node.size()];
                int j =0;
                while (subElements.hasNext()){
                    s[j] = subElements.next().asInt();
                    j++;
                }
                row.setField(i,s);
            }
            else if(typeInformation == Types.LONG){
                long[] s = new long[node.size()];
                int j =0;
                while (subElements.hasNext()){
                    s[j] = subElements.next().asLong();
                    j++;
                }
                row.setField(i,s);

           }else if(typeInformation.toString().startsWith("Row(")){
                LOGGER.info("Array数组嵌套类，子类型为ROW");
                //需要根据子子结构定义，再次封装对象类 字段名 和 属性
                String pId = dsSchemaColumns.get(0).getVirtualId();
                List<DsSchemaColumn> subDsSchemaColumns =  dsSchemaColumnsConvertBasic(levelList, pId);

                List<String> subFieldNames = new LinkedList<>();
                List<TypeInformation> subTypeInformations = new LinkedList<>();

                for (DsSchemaColumn d:subDsSchemaColumns) {
                    subFieldNames.add(d.getName());
                    subTypeInformations.add(d.getTypeInformation());
                }
                String[] subFieldNameArray = subFieldNames.stream().toArray(String[]::new);
                TypeInformation[] subTypeInformationArry = subTypeInformations.stream().toArray(TypeInformation[]::new);

                Row[] s = new Row[node.size()];
                int j =0;
                while (subElements.hasNext()){
                    String childJson = objectMapper.writeValueAsString(subElements.next());

                    Row subRow = convertToRow(childJson,subFieldNameArray,subTypeInformationArry,levelList,dsSchemaColumn.getVirtualId());
                    s[j] = subRow;
                    j++;
                }
                row.setField(i,s);
//            }else if(typeInformation == Types.OBJECT_ARRAY){

            }
            //todo 如果是多层数据，太尼玛复杂了
        }else{
            LOGGER.error("schema定义有误，Array类型下无嵌套定义，请检查,虚拟ID：{}",dsSchemaColumn.getVirtualId());
        }
    }

    /**
     * 根据key提取JsonNode
     * @param jsonNode
     * @param key
     * @return
     */
    public JsonNode getIgnoreCase(JsonNode jsonNode, String key) {
        Iterator<String> iter = jsonNode.fieldNames();
        while (iter.hasNext()) {
            String key1 = iter.next();
            if (key1.equalsIgnoreCase(key)) {
                return jsonNode.get(key1);
            }
        }
        return null;
    }

    /**
     * 获取维表嵌套需要的信息
     * @param ds
     * @return
     */
    public Map getSideTableInfo(Ds ds) {

        //获取json属性列值 k-v
        List<JsonField> jsonFields = GsonUtils.jsonToList(ds.getJsonValue(),JsonField.class);
        Map fieldMap = jsonFieldCollect(jsonFields);

        //带层级的列投影
        List<DsSchemaColumn> dsSchemaColumnList = ds.getDsSchemaColumnList();

        //需要的单级别列投影
        List<DsSchemaColumn> subList = dsSchemaColumnsConvertBasic(dsSchemaColumnList,"0");

        String[] fieldNames = new String[subList.size()];
        List<String> connectionFields = new ArrayList<>();

        TypeInformation[] typeInformations = new TypeInformation[subList.size()];

        for (int i = 0; i < subList.size(); i++) {
            DsSchemaColumn dsSchemaColumn = subList.get(i);
            fieldNames[i] = dsSchemaColumn.getName();
            if(dsSchemaColumn.getJoinKey() == 1){
                connectionFields.add(dsSchemaColumn.getName());
            }
            typeInformations[i] = dsSchemaColumn.getTypeInformation();
        }

        //缓存参数
        CacheParm caffeineCache = cacheParmBuilder(fieldMap);

        Map sideTableMap = Maps.newHashMap();
        sideTableMap.put("fieldMap",fieldMap);
        sideTableMap.put("fieldNames",fieldNames);
        sideTableMap.put("typeInformations",typeInformations);
        sideTableMap.put("connectionFields", connectionFields);
        sideTableMap.put("caffeineCache",caffeineCache);

        return sideTableMap;
    }

    /**
     * json k-v 提取收集成map
     * @param jsonFields
     * @return
     */
    private Map jsonFieldCollect(List<JsonField> jsonFields) {
        Map<String,Object> map = Maps.newHashMap();
        for (JsonField jf: jsonFields) {
            String fieldVal = StrUtil.isNotEmpty(jf.getFieldValue())?jf.getFieldValue():jf.getDefaultValue();
             map.put(jf.getFieldName(), fieldVal);
             List<Option> options = jf.getOption();
             if(null != options){
                 for (Option option : options) {
                     //匹配选项值，判断是否有及联项
                    if(fieldVal.equals(option.getName()) && option.isHasUnion()){
                       List<UnionField> unionFields =option.getUnionFields();
                        for (UnionField unionField : unionFields) {
                            map.put(unionField.getFieldName(),StrUtil.isNotEmpty(unionField.getFieldValue())?unionField.getFieldValue():unionField.getDefaultValue());
                        }
                    }
                 }
             }
        }
//        jsonFields.stream().collect(Collectors.toMap(JsonField::getFieldName, JsonField::getFieldValue, (key1, key2) -> key2, LinkedHashMap::new));
      return  map;
    }

    /**
     * 通过json模版数据提取的map构建缓存参数，用于维表缓存参数传递
     * @param fieldMap
     * @return
     */
    private CacheParm cacheParmBuilder(Map fieldMap) {

        String cacheType = null != fieldMap.get("cacheType") ? (String)fieldMap.get("cacheType"): CacheTypeEnum.NONE.getCacheType();
        int initialCapacity = null != fieldMap.get("initialCapacity") ? Integer.parseInt((String) fieldMap.get("initialCapacity")) : 1 ;
        long maximumSize = null != fieldMap.get("maximumSize") ? Long.parseLong((String)fieldMap.get("maximumSize")) : 5000;
        long expireAfterWrite = null != fieldMap.get("expireAfterWrite") ? Long.parseLong((String)fieldMap.get("expireAfterWrite")) : 60;

        return  CacheParm.Builder.newBuilder()
                .setCacheType(cacheType)
                .setInitialCapacity(initialCapacity)
                .setMaximumSize(maximumSize)
                .setExpireAfterWrite(expireAfterWrite)
                .build();
    }

    /**
     * 将递归集合DsSchemaColumnVOList转换为编码需要的列一级属性和字段名
     * @param levelList
     * @param pId
     * @return
     */
    List<DsSchemaColumn> dsSchemaColumnsConvertBasic(List<DsSchemaColumn> levelList, String pId){

        //根级
        if("0".equals(pId)){
            AtomicReference<String> virtualId = new AtomicReference<>("1");
            levelList.forEach(dsSchemaColumn -> {
                if(pId.equals(dsSchemaColumn.getVirtualPid())
                        && "Row".equals(dsSchemaColumn.getBasicType())){
                    virtualId.set(dsSchemaColumn.getVirtualId());
                }
            });

            //找一级子节点，即列属性
            return dsSchemaColumnsConvertBasic(levelList,virtualId.get());
        }else{
            List subList = new ArrayList();

            levelList.forEach(dsSchemaColumn -> {
                if(pId.equals(dsSchemaColumn.getVirtualPid())){

                    String basicType = dsSchemaColumn.getBasicType();

                    //实体类型
                    if("Row".equals(basicType)){

                        List<DsSchemaColumn> dsSchemaColumns =  dsSchemaColumnsConvertBasic(levelList, dsSchemaColumn.getVirtualId());

                        List<String> fieldNames = new LinkedList<>();
                        List<TypeInformation> typeInformations = new LinkedList<>();

                        for (DsSchemaColumn d:dsSchemaColumns) {
                            fieldNames.add(d.getName());
                            typeInformations.add(d.getTypeInformation());
                        }
                        String[] fieldNameArray = fieldNames.stream().toArray(String[]::new);
                        TypeInformation[] typeInformationArry = typeInformations.stream().toArray(TypeInformation[]::new);

                        if(typeInformationArry.length > 1 && typeInformationArry.length == fieldNameArray.length){
                            TypeInformation t = Types.ROW_NAMED(fieldNameArray,typeInformationArry);
                            dsSchemaColumn.setTypeInformation(t);
                        }

                    }
                    //数组类型
                    else if(basicType.contains("List")){

                        List<DsSchemaColumn> dsSchemaColumns =  dsSchemaColumnsConvertBasic(levelList, dsSchemaColumn.getVirtualId());

                        TypeInformation typeInformation;
                        if(dsSchemaColumns.size()>0){
                            typeInformation = dsSchemaColumns.get(0).getTypeInformation();
                            TypeInformation t =Types.OBJECT_ARRAY(typeInformation);
                            dsSchemaColumn.setTypeInformation(t);
                        }
                    }else{
                        TypeInformation t = string2TypeInformation(basicType);
                        dsSchemaColumn.setTypeInformation(t);
                    }

                    subList.add(dsSchemaColumn);
                }
            });

            return subList;
        }
    }

    /**
     * 字符串类型转为FLINK BasicType
     * @param fieldType
     * @return
     */
    private static TypeInformation string2TypeInformation(String fieldType){

        TypeInformation typeInformation = Types.STRING;
        switch (fieldType){
            case "Void":
                typeInformation = Types.VOID;
                break;
            case "String":
                typeInformation = Types.STRING;
                break;
            case "Byte":
                typeInformation = Types.BYTE;
                break;
            case "Boolean":
                typeInformation = Types.BOOLEAN;
                break;
            case "Short":
                typeInformation = Types.SHORT;
                break;
            case "Integer":
                typeInformation = Types.INT;
                break;
            case "Long":
                typeInformation = Types.LONG;
                break;
            case "Float":
                typeInformation = Types.FLOAT;
                break;
            case "Double":
                typeInformation = Types.DOUBLE;
                break;
            case "Character":
                typeInformation = Types.CHAR;
                break;
            case "BigDecimal":
                typeInformation = Types.BIG_DEC;
                break;
            case "BigInteger":
                typeInformation = Types.BIG_INT;
                break;
            case "Date":
                typeInformation = Types.SQL_DATE;
                break;
            case "Time":
                typeInformation = Types.SQL_TIME;
                break;
            case "Timestamp":
                typeInformation = Types.SQL_TIMESTAMP;
                break;
            case "LocalDate":
                typeInformation = Types.LOCAL_DATE;
                break;
            case "LocalDateTime":
                typeInformation = Types.LOCAL_DATE_TIME;
                break;
            case "LocalTime":
                typeInformation = Types.LOCAL_TIME;
                break;
            case "Instant":
                typeInformation = Types.INSTANT;
                break;
            default:
                break;
        }

        return typeInformation;
    }

    /**
     * jsonschema 转换为列投影 测试专用  job_admin flinkutils 已有该方法
     * @param nodeName
     * @param jsonStr
     * @param level
     * @param pID
     * @param list
     * @return
     * @throws Exception
     */
    @Deprecated
    public static List<DsSchemaColumn> jsonSchemaTypesConvert(String nodeName, String jsonStr, Integer level, String pID, List<DsSchemaColumn> list) throws Exception {

        if(null == list){
            list = new ArrayList<>();
        }
        com.google.gson.JsonObject typesJsonObject = JsonParser.parseString(jsonStr).getAsJsonObject();

        String type = "";
        String format = "";
        String encoding = "";

        if(typesJsonObject.has("type")){
            type = typesJsonObject.get("type").getAsString().toLowerCase();
        }
        if(typesJsonObject.has("format")){
            format = typesJsonObject.get("format").getAsString().toLowerCase();
        }
        if(typesJsonObject.has("encoding")) {
            encoding = typesJsonObject.get("encoding").getAsString().toLowerCase();
        }
        //校验，不支持根级是array类型
        if(level == 0 && !"object".equals(type)){
            throw new Exception("根级type必须是object类型");
        }

        JsonSchemaTypesEnum jsonSchemaType = JsonSchemaTypesEnum.getByType(type,format,encoding);

        String flinkSqlType = "";
        boolean flinkSqlUnsupported = false;
        String basicType = "";
        if(null != jsonSchemaType){
            flinkSqlType = jsonSchemaType.getFlinkSQLTypesEnum() == null ? "":jsonSchemaType.getFlinkSQLTypesEnum().getType();
            flinkSqlUnsupported = jsonSchemaType.getFlinkSQLTypesEnum() == null ? false:jsonSchemaType.getFlinkSQLTypesEnum().isUnsupported();
            basicType = jsonSchemaType.getTypeInformation() == null ? "":jsonSchemaType.getTypeInformation().toString();
        }

        String id = String.valueOf(IdUtils.getId()); //分配虚拟ID

        DsSchemaColumn dsSchemaColumnVO = new DsSchemaColumn();

        dsSchemaColumnVO.setVirtualId(id);
        dsSchemaColumnVO.setVirtualPid(pID);
        dsSchemaColumnVO.setName(nodeName);
        dsSchemaColumnVO.setBasicType(basicType);
        if(flinkSqlUnsupported){
            dsSchemaColumnVO.setFlinkType(flinkSqlType);
        }
        dsSchemaColumnVO.setLevel(level);

        list.add(dsSchemaColumnVO);

        //递归
        level++;
        if(flinkSqlType.equals(FlinkSQLTypesEnum.ROW.toString())){

            com.google.gson.JsonObject properties = typesJsonObject.getAsJsonObject("properties");
            for (Map.Entry<String, JsonElement> entry : properties.entrySet()){
                jsonSchemaTypesConvert(entry.getKey(),GsonUtils.gsonString(entry.getValue()),level,id,list);
            }
        }else if(flinkSqlType.equals(FlinkSQLTypesEnum.ARRAY.toString())){
            JsonArray jsonArray = typesJsonObject.getAsJsonArray("items");
            com.google.gson.JsonObject jsonObject = jsonArray.get(0).getAsJsonObject();
            jsonSchemaTypesConvert("array_node_"+nodeName,GsonUtils.gsonString(jsonObject),level,id,list);
        }

        return list;
    }

    /**
     * 类型批量转换
     * @param fieldTypes
     * @return
     */
    private TypeInformation[] string2TypeInformations(List<String> fieldTypes) {

        TypeInformation[] typeInformations = fieldTypes.stream().map(f ->{

            return string2TypeInformation(f);

        }).collect(Collectors.toList()).stream().toArray(TypeInformation[] :: new);

        return typeInformations;
    }


    public static void main(String[] args) throws Exception {

        String s = "{\n" +
                "  \"type\": \"object\",\n" +
                "  \"properties\": {\n" +
                "    \"lon\": {\n" +
                "      \"type\": \"number\"\n" +
                "    },\n" +
                "    \"info\": {\n" +
                "      \"type\": \"object\",\n" +
                "      \"properties\": {\n" +
                "        \"city\" : {\n" +
                "          \"type\": \"string\"\n" +
                "        },\n" +
                "        \"like\" : {\n" +
                "          \"type\": \"string\"\n" +
                "        },\n" +
                "        \"play\" : {\n" +
                "          \"type\": \"string\"\n" +
                "        }\n" +
                "      }\n" +
                "    },\n" +
                "    \"info2\": {\n" +
                "      \"type\": \"array\",\n" +
                "      \"items\": [{\n" +
                "        \"type\": \"object\",\n" +
                "        \"properties\": {\n" +
                "          \"city2\": {\n" +
                "            \"type\": \"string\"\n" +
                "          },\n" +
                "          \"like2\": {\n" +
                "            \"type\": \"string\"\n" +
                "          },\n" +
                "          \"play2\": {\n" +
                "            \"type\": \"string\"\n" +
                "          }\n" +
                "        }\n" +
                "      }]\n" +
                "    },\n" +
                "    \"info3\": {\n" +
                "      \"type\": \"array\",\n" +
                "      \"items\": [{\n" +
                "        \"type\": \"string\"\n" +
                "      }]\n" +
                "    },\n" +
                "    \"rideTime\": {\n" +
                "      \"type\": \"string\",\n" +
                "      \"format\": \"date-time\"\n" +
                "    }\n" +
                "  }\n" +
                "}";

        TranslationUtils translationUtils = new TranslationUtils();
        List<DsSchemaColumn> list = TranslationUtils.jsonSchemaTypesConvert("root",s,0,"0",null);
        list.forEach(dsSchemaColumnVO -> {
            System.out.println(dsSchemaColumnVO.getVirtualPid()+"--"+dsSchemaColumnVO.getVirtualId()+"--"+dsSchemaColumnVO.getLevel()+"--"+GsonUtils.gsonString(dsSchemaColumnVO));
        });

        System.out.println("------------------");

        List<DsSchemaColumn> subList = translationUtils.dsSchemaColumnsConvertBasic(list,"0");
        subList.forEach(dsSchemaColumnVO -> {
            TypeInformation t = dsSchemaColumnVO.getTypeInformation();
            System.out.println(dsSchemaColumnVO.getVirtualPid()+"--"+dsSchemaColumnVO.getVirtualId()+"--"+dsSchemaColumnVO.getLevel()+"--"+t);
        });
    }
}
