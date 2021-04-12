package com.haibao.udf;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import org.apache.flink.table.functions.ScalarFunction;

/*
 * @Author ml.c
 * @Description 从json字符串获取某个特定路径的值
 * @Date 15:19 2020-04-13
 **/
public class json_path  extends ScalarFunction {

    public int eval(String ... strs) {

        String json = strs[0];
        Object document = Configuration.defaultConfiguration().jsonProvider().parse(json);

//        String author0 = JsonPath.read(document, "$.store.book[0].author");
        return JsonPath.read(document, "$."+strs[1]);
    }

    public static void main(String[] args) {
        System.out.println("...");
    }
}
