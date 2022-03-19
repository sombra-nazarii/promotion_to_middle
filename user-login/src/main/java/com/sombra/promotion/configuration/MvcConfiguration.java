package com.sombra.promotion.configuration;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.Duration;

@Configuration
public class MvcConfiguration implements WebMvcConfigurer {

    @Bean
    @LoadBalanced
    public RestTemplate userAppRestTemplate(final RestTemplateBuilder builder) {
        return getDefaultRestTemplateBuilder(builder)
                .build();
    }

    private static RestTemplateBuilder getDefaultRestTemplateBuilder(final RestTemplateBuilder builder) {
        return builder
                .requestFactory(SimpleClientHttpRequestFactory::new)
                .setConnectTimeout(Duration.ofSeconds(10))
                .setReadTimeout(Duration.ofSeconds(10));
    }
}
