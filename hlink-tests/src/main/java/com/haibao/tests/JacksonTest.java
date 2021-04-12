package com.haibao.tests;/*
 * @Author ml.c
 * @Description //TODO
 * @Date 17:31 2020-04-13
 **/

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.Iterator;
import java.util.Map;

public class JacksonTest {

    public static void main(String[] args) throws JsonProcessingException {
        // 实例化 ObjectMapper 对象
        ObjectMapper objectMapper = new ObjectMapper();

        // json 消息
        String json = "{\"firstname\":\"Bo\",\"lastname\":\"Shang\",\"age\":30,\"arr\":[\"11\",\"22\"]}";

        // 将 json 转成 JsonNode 对象
        JsonNode rootNode = objectMapper.readTree(json);

        JsonNode arrNode = rootNode.get("arr");
        Iterator<JsonNode> s1 = arrNode.elements();
        System.out.println(arrNode.isArray());
        System.out.println(arrNode.size());
        System.out.println(s1.next().asText());
        System.out.println(s1.next().textValue());

        // 得到节点值
        JsonNode firstNameNode = rootNode.get("firstname");
        System.out.println("firstname:" + firstNameNode.asText());

        JsonNode ageNode = rootNode.get("age");
        System.out.println("age:" + ageNode.asInt());

        // 创建新节点
        ObjectNode newNode = objectMapper.createObjectNode();
        newNode.setAll((ObjectNode)rootNode);
        newNode.put("hometown", "dalian");

        // 将 JsonNode 对象转成 json
        String newjson = objectMapper.writeValueAsString(newNode);
        System.out.println(newjson);

    }
}
