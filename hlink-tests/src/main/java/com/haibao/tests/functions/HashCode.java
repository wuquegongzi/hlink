package com.haibao.tests.functions;

import org.apache.flink.table.functions.ScalarFunction;

/*
 * @Author ml.c
 * @Description 标量函数 HashCode
 * @Date 11:04 2020-05-12
 **/
public class HashCode extends ScalarFunction {
    private int factor = 12;

    public HashCode() {
    }

    public int eval(String s) {
        return s.hashCode() * factor;
    }
}
