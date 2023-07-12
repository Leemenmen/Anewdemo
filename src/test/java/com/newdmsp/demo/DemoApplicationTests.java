package com.newdmsp.demo;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.AsymmetricCrypto;
import cn.hutool.crypto.asymmetric.KeyType;
import com.newdmsp.demo.security.RsaUtil;
import lombok.extern.slf4j.Slf4j;
import org.jasypt.encryption.StringEncryptor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.annotation.Resource;
import java.security.KeyPair;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.newdmsp.demo.security.RsaUtil.getRSAPrivateKeyBybase64;
import static com.newdmsp.demo.security.RsaUtil.getRSAPublidKeyBybase64;

@SpringBootTest
@Slf4j
class DemoApplicationTests {

//    @Test
//    void contextLoads() {
//    }
    @Resource
    StringEncryptor encryptor;
    @Resource
    RsaUtil rsaUtil;

    @Test
    public void encrypt() throws Exception {
//        String url = encryptor.encrypt("jdbc:mysql://localhost:3306/dmsp?characterEncoding=utf-8&serverTimezone=UTC&allowMultiQueries=true");
//        String username = encryptor.encrypt("root");
//        String pwd = encryptor.encrypt("2021203");
//        System.out.println("url = " + url);
//        System.out.println("username = " + username);
//        System.out.println("pwd = " + pwd);

//        rsaUtil.genKey();
//
//        AsymmetricCrypto asymmetricCrypto = new AsymmetricCrypto("RSA", getRSAPrivateKeyBybase64("私钥解密"),
//                getRSAPublidKeyBybase64("公钥加密"));
//
//        String s1 = asymmetricCrypto.encryptBase64("你好呀", KeyType.PublicKey);
//        log.info(String.valueOf(KeyType.PublicKey));
//        log.info("加密后");
//        System.out.println(s1);
//
//        String s = asymmetricCrypto.decryptStr(s1, KeyType.PrivateKey);
//        log.info(String.valueOf(KeyType.PrivateKey));
//        log.info("解密后");
//        System.out.println(s);

        String createDate = new SimpleDateFormat("yyyy_MM_dd").format(new Date());
        String createTime = new SimpleDateFormat("HH:mm:ss").format(new Date());
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        log.info(createTime);
        log.info(createDate);
        log.info(date);

    }


}
