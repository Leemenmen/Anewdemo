package com.newdmsp.demo.security;

import org.springframework.web.servlet.config.annotation.*;
import org.springframework.context.annotation.Configuration;

import static com.newdmsp.demo.utils.Config.ROOT_PATH;

@Configuration
public class MyWebMvcConfigurer implements WebMvcConfigurer {



    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**","/ace-builds-master/**","/fonts/**")
                .addResourceLocations("classpath:/static/","classpath:/static/ace-builds-master/","classpath:/static/fonts/");
        registry.addResourceHandler("/js/**").addResourceLocations("classpath:/static/js/");
        registry.addResourceHandler("/lib/**").addResourceLocations("classpath:/static/lib/");
        registry.addResourceHandler("/css/**").addResourceLocations("classpath:/static/css/");
        registry.addResourceHandler("/images/**").addResourceLocations("classpath:/static/images/");
        registry.addResourceHandler("/pictures/**").addResourceLocations("file:///"+ROOT_PATH+"/stuCode/");
        registry.addResourceHandler("/resultPic/**").addResourceLocations("file:///"+ROOT_PATH);
//
//        registry.addResourceHandler("/pictures/**").addResourceLocations("file:/opt/pubWebPro/stuCode/");
//        registry.addResourceHandler("/resultPic/**").addResourceLocations("file:/opt/pubWebPro/");


//        registry.addResourceHandler("/ac/**").addResourceLocations("classpath:/ac/**");


        WebMvcConfigurer.super.addResourceHandlers(registry);
    }
}
