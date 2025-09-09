package com.newdmsp.demo.security;

import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.MimeMappings;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TomcatConfig {
    @Bean
    public ConfigurableServletWebServerFactory configurableServletWebServerFactory() {
        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory();

        MimeMappings mappings = new MimeMappings(MimeMappings.DEFAULT);
        mappings.add("wasm", "application/wasm");
        tomcat.setMimeMappings(mappings);

        return tomcat;
    }
}
