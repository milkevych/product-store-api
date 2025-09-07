package org.projects.orderservice.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;

import feign.RequestInterceptor;

@Configuration
public class FeignClientConfig{

    @Bean
    public RequestInterceptor requestInterceptor() {
        return template -> {
            var auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.getCredentials() instanceof String jwt) {
                template.header(HttpHeaders.AUTHORIZATION, "Bearer " +jwt);
            }
        };
    }

}
