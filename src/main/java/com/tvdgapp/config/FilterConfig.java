package com.tvdgapp.config;

import io.github.bucket4j.Bucket;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<RateLimitingFilter> loggingFilter(Bucket bucket) {
        FilterRegistrationBean<RateLimitingFilter> registrationBean = new FilterRegistrationBean<>();

        registrationBean.setFilter(new RateLimitingFilter(bucket));
        registrationBean.addUrlPatterns("/api/*"); // Apply to all API endpoints

        return registrationBean;
    }
}
