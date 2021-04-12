package com.haibao.admin;

import org.jasypt.encryption.StringEncryptor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @ClassName EncodeTest
 * @Description
 * @Author ml.c
 * @Date 2020/3/3 11:49 上午
 * @Version 1.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class EncodeTest {

    @Autowired
    StringEncryptor stringEncryptor;

    @Test
    public void encryptPwd() {
        //加密密码
        String pwd = stringEncryptor.encrypt("123456");
        System.out.println(pwd);
    }
}
