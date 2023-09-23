package com.example.voter_engine.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class corsConfig {

    @Bean
    public WebMvcConfigurer webMvcConfigure(){

        return new WebMvcConfigurer(){

            @Override
            public void addCorsMappings(CorsRegistry registry){
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:3000")
                        .allowedHeaders("GET","PUT","POST","DELETE")
                        .allowedHeaders("*");
            }

        };
    }
}
