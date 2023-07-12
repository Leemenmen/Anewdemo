package com.newdmsp.demo.security.user;

import org.springframework.security.core.AuthenticationException;

public class UserNotFoundAndBaned extends AuthenticationException {
    public UserNotFoundAndBaned(String msg){
        super(msg);
    }
}
