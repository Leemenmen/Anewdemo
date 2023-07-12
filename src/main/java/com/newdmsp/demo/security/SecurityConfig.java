package com.newdmsp.demo.security;

import com.newdmsp.demo.security.user.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.Resource;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    @Resource
    private CustomUserDetailsService customUserDetailsService;
    @Resource
    private CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
    @Resource
    private CustomAuthenticationFailHandler customAuthenticationFailHandler;
    @Resource
    private CustomAccessDeniedHandler customAccessDeniedHandler;



    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        //将自定的CustomUserDetailsService装配到AuthenticationManagerBuilder
        auth.userDetailsService(customUserDetailsService).passwordEncoder(new CustomPasswordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
//                .httpBasic().disable()
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                .and()
                .authorizeRequests()
                .antMatchers("/login","/loginTrue","/login/get/pub-key", "/ac","/figureTest").permitAll()
//                .antMatchers(HttpMethod.GET, "/admin").hasRole("ADMIN")
//                .antMatchers(HttpMethod.GET, "/index").hasRole("USER")
                .antMatchers(HttpMethod.GET, "/admin","/doc.html").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/index").hasRole("USER")
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/loginTrue")
                .successHandler(customAuthenticationSuccessHandler)
                .failureHandler(customAuthenticationFailHandler)
                .usernameParameter("username")
                .passwordParameter("password")
                .and()
                //登出配置
                .logout()
                .permitAll()
                .logoutSuccessUrl("/")
                .deleteCookies("JSESSIONID")
                .and()
                .exceptionHandling()
                .accessDeniedHandler(customAccessDeniedHandler) //无权限时处理
                .and()
                .csrf().disable()
                .headers().frameOptions().sameOrigin(); //项目中使用iframe框架时需配置;
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        //解决静态资源被拦截的问题
        web.ignoring()
//                .antMatchers("/login")
//                .antMatchers("/loginTrue")
                .antMatchers("/static/**")
                .antMatchers("/js/**")
                .antMatchers("/images/**")
                .antMatchers("/css/**")
                .antMatchers("/lib/**")
                .antMatchers("/ace-builds-master/**")
                .antMatchers("/fonts/**")
                .antMatchers("/pictures/**")
                .antMatchers("/resultPic/**")
        ;
    }
}

