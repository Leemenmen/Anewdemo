package com.newdmsp.demo.security.user;

import com.newdmsp.demo.entity.User;
import com.newdmsp.demo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Resource
    UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        log.info(username);
        User user = userService.getUserInfo1(username);
//        log.info(String.valueOf(user));
        //如果用户不存在直接抛出UsernameNotFoundException异常
        if (user == null) {
//            throw new UsernameNotFoundException("用户不存在");
            throw new UserNotFoundAndBaned("用户不存在");
        }
        if (user.getEnable() == 0){

            throw new UserNotFoundAndBaned("账号不可用,请联系授课老师启用!");
        }
        String role = null;
        if (user.getRoleid() == 1) {
            role = "ROLE_USER";
        } else {
            role = "ROLE_ADMIN";
        }

//        System.out.println(username);
        //声明一个用于存放用户权限的列表
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
//        //获取该用户所拥有的权限
//        List<Permission> authority = permissionService.selectListByUserId(user.getId());
//        //获取该用户所属角色
//        List<Role> role = permissionService.selectRoleListByUserId(user.getId());
//        //把用户所拥有的权限添加到列表中
//        authority.forEach(permission -> {
//            grantedAuthorities.add(new SimpleGrantedAuthority(permission.getAuthority()));
//        });
        //把用户角色加到列表中
//        role.forEach(role1 -> {
//            //注意：添加角色的时候要在前面加ROLE_前缀
//            grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_"+role1.getRole_name()));
//        });
        grantedAuthorities.add(new SimpleGrantedAuthority(role));

//        List grantedAuthorities = new ArrayList<>();

//        log.info(String.valueOf(new org.springframework.security.core.userdetails.User(user.getUsername(),user.getPassword(), grantedAuthorities )));
        //创建并返回User对象，注意这里的User不是我们实体类里面的User
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), grantedAuthorities);

    }
}
