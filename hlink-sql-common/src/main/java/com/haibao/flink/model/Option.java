package com.haibao.flink.model;

import java.util.List;

/**
 * @ClassName Option
 * @Author ml.c
 * @Date 2020/2/26 7:54 下午
 * @Version 1.0
 */
public class Option {

    private String name;
    private String dName;
    private boolean hasUnion;
    private List<UnionField> unionFields;

    public Option(String name, String dName, boolean hasUnion, List<UnionField> unionFields) {
        this.name = name;
        this.dName = dName;
        this.hasUnion = hasUnion;
        this.unionFields = unionFields;
    }

    public Option() {
    }

    public List<UnionField> getUnionFields() {
        return unionFields;
    }

    public void setUnionFields(List<UnionField> unionFields) {
        this.unionFields = unionFields;
    }

    public boolean isHasUnion() {
        return hasUnion;
    }

    public void setHasUnion(boolean hasUnion) {
        this.hasUnion = hasUnion;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getdName() {
        return dName;
    }

    public void setdName(String dName) {
        this.dName = dName;
    }
}
