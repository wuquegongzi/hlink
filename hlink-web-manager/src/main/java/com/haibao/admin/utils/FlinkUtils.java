package com.haibao.admin.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.haibao.admin.web.vo.DsSchemaColumnVO;
import com.haibao.flink.enums.FlinkSQLTypesEnum;
import com.haibao.flink.enums.JsonSchemaTypesEnum;
import com.haibao.flink.utils.GsonUtils;
import com.haibao.flink.utils.IdUtils;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @ClassName FlinkUtils
 * @Description Flink 工具类
 * @Author ml.c
 * @Date 2020/2/14 8:37 下午
 * @Version 1.0
 */
public class FlinkUtils {

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

        List<DsSchemaColumnVO> list = FlinkUtils.jsonSchemaTypesConvert("root",s,0,"0",null);
        list.forEach(dsSchemaColumnVO -> {
            System.out.println(dsSchemaColumnVO.getVirtualPid()+"--"+dsSchemaColumnVO.getVirtualId()+"--"+dsSchemaColumnVO.getLevel()+"--"+GsonUtils.gsonString(dsSchemaColumnVO));
        });

        System.out.println("------------------");

        List<DsSchemaColumnVO> subList = dsSchemaColumnsConvertDDL(list,"0");
        subList.forEach(dsSchemaColumnVO -> {
            System.out.println(dsSchemaColumnVO.getVirtualPid()+"--"+dsSchemaColumnVO.getVirtualId()+"--"+dsSchemaColumnVO.getLevel()+"--"+GsonUtils.gsonString(dsSchemaColumnVO));
        });
    }

    /**
     * 提炼jsonschema,转换为递归结构的列投影
     * @param jsonStr
     * @param level
     * @param pID
     * @param list
     * @return
     */
    public static List<DsSchemaColumnVO> jsonSchemaTypesConvert(
            String nodeName, String jsonStr, Integer level, String pID,
            List<DsSchemaColumnVO> list) throws Exception {

        if(null == list){
            list = new ArrayList<>();
        }
        JsonObject typesJsonObject = JsonParser.parseString(jsonStr).getAsJsonObject();

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

        //分配虚拟ID
        String id = String.valueOf(IdUtils.getId());

        DsSchemaColumnVO dsSchemaColumnVO = new DsSchemaColumnVO();

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

            JsonObject properties = typesJsonObject.getAsJsonObject("properties");
            for (Map.Entry<String, JsonElement> entry : properties.entrySet()){
                jsonSchemaTypesConvert(entry.getKey(),GsonUtils.gsonString(entry.getValue()),level,id,list);
            }
        }else if(flinkSqlType.equals(FlinkSQLTypesEnum.ARRAY.toString())){
            JsonArray jsonArray = typesJsonObject.getAsJsonArray("items");
            JsonObject jsonObject = jsonArray.get(0).getAsJsonObject();
            jsonSchemaTypesConvert("array_node_"+nodeName,GsonUtils.gsonString(jsonObject),level,id,list);
        }

        return list;
    }

    /**
     * 将递归集合DsSchemaColumnVOList转换为DDL需要的列一级属性
     * @param levelList
     * @param pId
     * @return
     */
    public static List<DsSchemaColumnVO> dsSchemaColumnsConvertDDL(List<DsSchemaColumnVO> levelList, String pId){

        //根级
        if("0".equals(pId)){
            AtomicReference<String> virtualId = new AtomicReference<>("1");
            levelList.forEach(dsSchemaColumnVO -> {
                if(pId.equals(dsSchemaColumnVO.getVirtualPid())
                        && "ROW".equals(dsSchemaColumnVO.getFlinkType())){
                    virtualId.set(dsSchemaColumnVO.getVirtualId());
                }
//                else if(pId == dsSchemaColumnVO.getVirtualPid()
//                        && dsSchemaColumnVO.getFlinkType().equals("ARRAY")){
//                    //根级是数组 不做支持
//                }
            });

            //找一级子节点，即ddl列属性
            return dsSchemaColumnsConvertDDL(levelList,virtualId.get());
        }else{
            List subList = new ArrayList();

            levelList.forEach(dsSchemaColumnVO -> {
                if(pId.equals(dsSchemaColumnVO.getVirtualPid())){

                    String flinkSqlType = dsSchemaColumnVO.getFlinkType();

                    if(FlinkSQLTypesEnum.ROW.getType().equals(flinkSqlType)){

                        List<DsSchemaColumnVO> dsSchemaColumnVOs =  dsSchemaColumnsConvertDDL(levelList, dsSchemaColumnVO.getVirtualId());

                        StringBuffer sb = new StringBuffer();
                        int i =1;
                        for (DsSchemaColumnVO d:dsSchemaColumnVOs) {
                            sb.append(d.getName()).append(" ").append(d.getFlinkType());
                            if(i < dsSchemaColumnVOs.size()){
                                sb.append(",");
                            }
                            i++;
                        }
                        if(sb.length()>0){
                            flinkSqlType += "<"+sb.toString() +">";
                            dsSchemaColumnVO.setFlinkType(flinkSqlType);
                        }

                   }else if(FlinkSQLTypesEnum.ARRAY.getType().equals(flinkSqlType)){
                        List<DsSchemaColumnVO> dsSchemaColumnVOs =  dsSchemaColumnsConvertDDL(levelList, dsSchemaColumnVO.getVirtualId());

                        StringBuffer sb = new StringBuffer();
                        int i =1;
                        for (DsSchemaColumnVO d:dsSchemaColumnVOs) {
                            sb.append(d.getFlinkType());
                            if(i < dsSchemaColumnVOs.size()){
                                sb.append(",");
                            }
                            i++;
                        }
                        if(sb.length()>0){
                            flinkSqlType += "<"+sb.toString() +">";
                            dsSchemaColumnVO.setFlinkType(flinkSqlType);
                        }
                   }

                  subList.add(dsSchemaColumnVO);
                }
            });

            return subList;
        }
    }
    /**
     * 根据schema映射基本数据类型和flink sql 数据类型
     * @param jsonSchema
     * @return
     */
    @Deprecated
    public static List<DsSchemaColumnVO> schemaConvertFields(String jsonSchema) {

        JsonObject jsonObject = JsonParser.parseString(jsonSchema).getAsJsonObject();
        JsonObject properties = jsonObject.getAsJsonObject("properties");

        Map<String,Map> keyMap = new LinkedHashMap();
        for (Map.Entry<String, JsonElement> entry : properties.entrySet()){
            //第一层 属性
            Map childMap = jsonSchemaTypesConvert(GsonUtils.gsonString(entry.getValue()),0);
            keyMap.put(entry.getKey(),childMap);
        }

        List<DsSchemaColumnVO> list = new ArrayList<>();
        for (String key:keyMap.keySet()) {

            Map map = keyMap.get(key);
            String flinkSqlType = "";
            boolean flinkSqlUnsupported = (Boolean) map.get("flinkSqlUnsupported");
            if(flinkSqlUnsupported){
                flinkSqlType = (String)map.get("flinkSqlType");
            }
            String basicType = (String) map.get("basicType");

            //需要拼接
            if(FlinkSQLTypesEnum.ROW.getType().equals(flinkSqlType) && null != map.get("data") && !"".equals(map.get("data"))){
                flinkSqlType += "<" + map2FlinkRow((Map)map.get("data")) + ">";
            }

            DsSchemaColumnVO dsSchemaColumnVO = new DsSchemaColumnVO();
            //属性名
            dsSchemaColumnVO.setName(key);
            //ddl sql 属性类型
            dsSchemaColumnVO.setFlinkType(flinkSqlType);
            //启用备用字段，基本数据类型
            dsSchemaColumnVO.setRes1(basicType);

            list.add(dsSchemaColumnVO);
        }

        return list;
    }

    /**
     * 转换row
     * @param map
     * @return
     */
    private static String map2FlinkRow(Map map) {

        StringBuffer sb = new StringBuffer();
        int length = map.size();
        int i =0;
        for (Object key:map.keySet()) {
            Map childMap = (Map)map.get(key);
            String basicType = (String)childMap.get("basicType");
            String flinkSqlType = (String)childMap.get("flinkSqlType");
            boolean flinkSqlUnsupported = (Boolean)childMap.get("flinkSqlUnsupported");
            int level = (Integer)childMap.get("level");
            Object obj = childMap.get("data");

            if(FlinkSQLTypesEnum.ROW.getType().equals(flinkSqlType) && null != obj && !"".equals(obj)){
                String col = map2FlinkRow((Map)obj);
                flinkSqlType += "<"+col+">";
            }
            i ++;
            sb.append(key).append(" ").append(flinkSqlType);
            if(i < length){
                sb.append(",");
            }
        }

        return sb.toString();
    }


    /**
     * jsonSchemaTypesConvert
     * 根据jsonschema 获取DDL 建表属性 以及基础类型
     * @param jsonStr
     * @return
     */
    @Deprecated
    private static Map jsonSchemaTypesConvert(String jsonStr,int level){

        JsonObject typesJsonObject = JsonParser.parseString(jsonStr).getAsJsonObject();

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

        JsonSchemaTypesEnum jsonSchemaType = JsonSchemaTypesEnum.getByType(type,format,encoding);

        String flinkSqlType = "";
        boolean flinkSqlUnsupported = false;
        String basicType = "";
        if(null != jsonSchemaType){
            flinkSqlType = jsonSchemaType.getFlinkSQLTypesEnum() == null ? "":jsonSchemaType.getFlinkSQLTypesEnum().getType();
            flinkSqlUnsupported = jsonSchemaType.getFlinkSQLTypesEnum() == null ? false:jsonSchemaType.getFlinkSQLTypesEnum().isUnsupported();
            basicType = jsonSchemaType.getTypeInformation() == null ? "":jsonSchemaType.getTypeInformation().toString();
        }

        level ++;

        Map<String,Object> map = new LinkedHashMap();
        map.put("basicType",basicType);
        map.put("flinkSqlType",flinkSqlType);
        map.put("flinkSqlUnsupported",flinkSqlUnsupported);
        map.put("level",level);

        //层级
        if(flinkSqlType.equals(FlinkSQLTypesEnum.ROW.toString())){
            //嵌套
            JsonObject properties = typesJsonObject.getAsJsonObject("properties");
            Map childMap = new HashMap(properties.size());
            for (Map.Entry<String, JsonElement> entry : properties.entrySet()){
                Map subMap = jsonSchemaTypesConvert(GsonUtils.gsonString(entry.getValue()),level);
                childMap.put(entry.getKey(),subMap);
            }
            map.put("data",childMap);
        }else if(flinkSqlType.equals(FlinkSQLTypesEnum.ARRAY.toString())){
            System.out.println("暂时不对数组做支持！");
            map.put("data","");
        }else{
            map.put("data","");
        }
        return map;
    }


    /**
     * 获取 ip和端口 拼接
     * @param clusterAddress
     * @return
     */
    public static String getIPAndPort(String clusterAddress) {

        if(clusterAddress.lastIndexOf("/")==clusterAddress.length()-1){
            clusterAddress=clusterAddress.substring(0,clusterAddress.lastIndexOf("/"));
        }
        clusterAddress = clusterAddress.replace("http://","").replace("https://","");

        return clusterAddress;
    }

    /**
     * baoyu
     * 路径获取
     * @param clusterAddress
     * @return
     */
    private String getBasePath(String clusterAddress){
        if(clusterAddress.lastIndexOf("/")==clusterAddress.length()-1){
            clusterAddress=clusterAddress.substring(0,clusterAddress.lastIndexOf("/"));
        }
        return clusterAddress;
    }

}
