package com.haibao.flink.utils;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.HexUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;

/**
 * @ClassName PwdAuthUtils
 * @Description 密码加密工具
 * @Author ml.c
 * @Date 2020/3/2 8:13 下午
 * @Version 1.0
 */
public class PwdAuthUtils {

    static String PRIVATE_KEY = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBALWjeArpAIk4e6Xdysu3Mp3v+Kr2y+5rW9iuxKdze6DBRVJHb3nHokkuYIl9YCTevVPvSbGmlMD7LUzFo1Ong7MVXJV47qRnKpZUIA8V6X/ZvYEeKlzhAVmSlNJ24HKHhWHw0yJ+TrFwUUizsQjA82mcNKAruXXJE9gaUQWVHweZAgMBAAECgYAWWSwab4us1oHEKTNb+EjmjNGUO3Rg+92bnfk9PyS9ZTpBwt9Q5QKvLWEcWYw8TRmPW7g9/uh7jAwJT6HmV4yJBYwx1JW2m506rj5kxcSCCgju+CJqmvO2tMsupKUSe0Pm73r/9vvnte6orpwQsGIioOPJrMwvopJyRG3ndr21JwJBAOdmkQ+qz8UqYIzMhYB7ySoC+jXiCRRPKleSqPp51YCL/abtW/dqzASucXqExClZhPRvWS6fkU+tQ6PItlDmig8CQQDI8qfjfk3mV/PCb0zwnaSzKISOS+QxlL/DVoCr45Xbe63eTj54q9bCDuxTxRfkhClLMjPfqMbIGfYTgUfldpvXAkBIy5ps7FZqd1diU+DXt5lkLwWZt98VVYZDgG7KRVOmGeQVBGy/HAjMnDg0y9mODIxn/TN4GFi1UROLTdQVBUF/AkBSa/1i+U54eYDZMo/Lbc29UpQ0TXqWv518QMHcPygbc3pYYZ2MH2eiBz72CTSInZDWcAwI5BBMySE+1NspimHXAkAqchRZc/s8ZQwnaRFCrkbewu8pECrQaBoWiU+BDs1n8p2cQq79hZoSERqw6B0z5omv79J+1D1kkXWP2eK7umVa";
    static String PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC1o3gK6QCJOHul3crLtzKd7/iq9svua1vYrsSnc3ugwUVSR295x6JJLmCJfWAk3r1T70mxppTA+y1MxaNTp4OzFVyVeO6kZyqWVCAPFel/2b2BHipc4QFZkpTSduByh4Vh8NMifk6xcFFIs7EIwPNpnDSgK7l1yRPYGlEFlR8HmQIDAQAB";


    /**
     * 私钥解密
     * @param text
     * @return
     */
    public String decrypt(String text){

        RSA rsa = new RSA(PRIVATE_KEY,null);

        byte[] aByte = HexUtil.decodeHex(text);
        byte[] decrypt = rsa.decrypt(aByte, KeyType.PrivateKey);

        return StrUtil.str(decrypt, CharsetUtil.CHARSET_UTF_8);
    }

    /**
     * 公钥加密
     * @param text
     * @return
     */
    public  String  encrypt(String text){

        RSA rsa = new RSA(null,PUBLIC_KEY);

        //公钥加密，私钥解密
        byte[] encrypt = rsa.encrypt(StrUtil.bytes(text, CharsetUtil.CHARSET_UTF_8), KeyType.PublicKey);

       return HexUtil.encodeHexStr(encrypt);
    }


    public static void main(String[] args) {

        PwdAuthUtils pwdAuthUtils = new PwdAuthUtils();
        String enStr = pwdAuthUtils.encrypt("123456");
        System.out.println(enStr);
        System.out.println(pwdAuthUtils.decrypt(enStr));
    }
}
