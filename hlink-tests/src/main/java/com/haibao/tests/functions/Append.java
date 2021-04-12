package com.haibao.tests.functions;

import org.apache.flink.table.functions.ScalarFunction;

/*
 * @Author ml.c
 * @Description //TODO
 * @Date 17:17 2020-05-12
 **/
public class Append extends ScalarFunction {

    public Append() {
    }

    public String eval(String s) {
        return s+"_110";
    }
}
