package com.newdmsp.demo.security.user;


import com.newdmsp.demo.security.RsaUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Slf4j
@Component
public class CustomPasswordEncoder implements PasswordEncoder {
//    @Resource
//    RsaUtil rsaUtil;

    @Override
    public String encode(CharSequence rawPassword) {
//        log.info((String) rawPassword);
        return rawPassword.toString();
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        log.info((String) rawPassword,encodedPassword);
        String realPassword = RsaUtil.decrypt((String) rawPassword);
        if (!encodedPassword.equals(realPassword)){
            throw new UserNotFoundAndBaned("密码错误");
        }else {
            return true;
        }
    }



}
