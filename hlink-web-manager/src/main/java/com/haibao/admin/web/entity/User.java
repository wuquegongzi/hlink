package com.haibao.admin.web.entity;

import lombok.Data;

/**
 * 测试专用
 */
@Data
//@TableName(value="user")
public class User {
//    @TableId(value="id")
    private Long id;
//    @TableField(value = "name")
    private String name;
//    @TableField(value = "age")
    private Integer age;
//    @TableField(value = "email")
    private String email;
}
