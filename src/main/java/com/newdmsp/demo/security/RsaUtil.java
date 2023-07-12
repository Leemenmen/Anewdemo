package com.newdmsp.demo.security;

import cn.hutool.core.codec.Base64Decoder;
import cn.hutool.core.codec.Base64Encoder;
import cn.hutool.crypto.SecureUtil;
import lombok.extern.slf4j.Slf4j;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Base64.Encoder;
import java.util.Base64.Decoder;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;


@Slf4j
@Component
public class RsaUtil   {
    /**
     * 私钥
     */
    private static final String PRIVATE_KEY = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAMa+UQp89KNWrJsZYN7MHUUhS65UXAE7xw+H/VSsqH8WR+WmARW/p1Y3SnDORyO6xo5esnNrzhUqhOoFo+of0iqWLxGeL5H43ohETSgOxeoePHypADyRNuFRitu8eaoOaB/N2EVZxi/ThM9Putqo+gwwhxrTv2V+5UjEbWQPK+ARAgMBAAECgYAW/DDMOTpj36TTxUIeUlX6RkkF7uy7GcpCYhRq5BMSq6WCCkpq1QEJ6BgcZsb0e2f2VD5oOeoaKZI4rJFSBP8dyJSkGLyMNjydPTWd19geCkSJ76hA6Iygamt9bv6qi39bFufeAf9Nq0GecbTcUu0DdNa8tD4vbqzqVN5E9LdvfwJBAOX4KyEhqPy6LViOnJRB2DYgafKfZOFsl9jX9bzAC8dBy8zMQ1hQc3jwb6GhkMIG4zmCOoFRb4V7iaoP8PWUAGcCQQDdPU/JhEtYjqFH42RVlXwq7nPgm6BRlvMfkabOSqGZabCoZ7GgAfLH3eVVu3W3SA7piUHU/eNvswnn3ha96fDHAkEAkz2/F5p+oTnvGLlOxZoW/ijozQCDTdYCr0MuwqhPokedXkYWhHAWTwPa5akVjzVrvQGFxhDHD7cWYH2OcR5O1wJAFDzmjo2OguRRNo1DgVyyRg8YBITX7n67cLjXJGnJTIpRgaYypmtnNQdEUkpao1BxJMXMgtpSssYS8uXq+KZswwJBAOKDx1XKWUgJjCzzTeKNEQnWfgK/OTWogWq29p53A+VJxcsqSJRzkEAa82mX2k1rUkUEJUdXfTJlRJC58rAuis4=";
    /**
     * 公钥
     */
    private static final String PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDGvlEKfPSjVqybGWDezB1FIUuuVFwBO8cPh/1UrKh/FkflpgEVv6dWN0pwzkcjusaOXrJza84VKoTqBaPqH9Iqli8Rni+R+N6IRE0oDsXqHjx8qQA8kTbhUYrbvHmqDmgfzdhFWcYv04TPT7raqPoMMIca079lfuVIxG1kDyvgEQIDAQAB";


    /**
     * 私钥解密
     *
     * @param encryptText 加密字符串
     * @return 解密字符串
     */
    public static String decrypt(String encryptText) {
        RSA rsa = new RSA(PRIVATE_KEY, null);
        byte[] decrypt = rsa.decrypt(encryptText, KeyType.PrivateKey);
        return new String(decrypt);
    }

    /**
     * 获取公钥
     *
     * @return 公钥
     */
    public static String getPublicKey() {
        return PUBLIC_KEY;
    }

    /**
     * 公钥、私钥生成方法
     */
    public void genKey() {
        RSA rsa = new RSA();

        String privateKeyBase64 = rsa.getPrivateKeyBase64();

        String publicKeyBase64 = rsa.getPublicKeyBase64();

        System.out.println(privateKeyBase64);
        System.out.println("-----------------------------");
        System.out.println(publicKeyBase64);

    }

    /**
     * @desc: 将字符串转换成RSAPublicKey类型
     * @date 2020-6-12 11:03:05
     * @param
     * @return
     */
    public static RSAPublicKey getRSAPublidKeyBybase64(String base64s) throws Exception {



        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(base64s));
        RSAPublicKey publicKey = null;
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        try {
            publicKey = (RSAPublicKey)keyFactory.generatePublic(keySpec);
        } catch (InvalidKeySpecException var4) {

        }
        return publicKey;
    }

    /**
     * @desc: 将字符串转换成RSAPrivateKey类型
     * @date 2020-6-12 11:03:01
     * @param
     * @return
     */
    public static RSAPrivateKey getRSAPrivateKeyBybase64(String base64s) throws Exception{
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(base64s));
        RSAPrivateKey privateKey = null;
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        try {
            privateKey = (RSAPrivateKey)keyFactory.generatePrivate(keySpec);
        } catch (InvalidKeySpecException var4) {
        }
        return privateKey;
    }

}

