package com.epam.ems.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {"com.epam.ems.web.controller"})
public class WebAppConfig implements WebMvcConfigurer {
    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor() {
        return new MethodValidationPostProcessor();
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(jacksonMessageConverter());
        WebMvcConfigurer.super.configureMessageConverters(converters);
    }

    @Bean
    public MappingJackson2HttpMessageConverter jacksonMessageConverter() {
        MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();

        List<MediaType> supportedMediaTypes = new ArrayList<>(messageConverter.getSupportedMediaTypes());
        messageConverter.setSupportedMediaTypes(supportedMediaTypes);
        supportedMediaTypes.add(MediaType.APPLICATION_OCTET_STREAM);
        messageConverter.setSupportedMediaTypes(supportedMediaTypes);
        messageConverter.setPrettyPrint(true);
        return messageConverter;
    }
}
