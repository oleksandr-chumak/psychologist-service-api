package com.service.psychologists.config;

import com.service.psychologists.core.resolvers.ParseOrderParamArgumentResolver;
import com.service.psychologists.core.resolvers.ParseSearchParamArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(new ParseSearchParamArgumentResolver());
        argumentResolvers.add(new ParseOrderParamArgumentResolver());
    }
}