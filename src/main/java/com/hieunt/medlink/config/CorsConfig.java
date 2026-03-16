package com.hieunt.medlink.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
public class CorsConfig {

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Cho phép localhost:4200 (Angular frontend)
        configuration.setAllowedOrigins(List.of("http://localhost:4200"));

        // Cho phép tất cả các HTTP methods
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));

        // Cho phép tất cả các headers
        configuration.setAllowedHeaders(Arrays.asList("*"));

        // Cho phép gửi credentials (cookies, authorization headers)
        configuration.setAllowCredentials(true);

        // Thời gian cache preflight request (1 giờ)
        configuration.setMaxAge(3600L);

        // Expose headers để frontend có thể đọc
        configuration.setExposedHeaders(Arrays.asList(
                "Authorization",
                "Content-Type",
                "X-Total-Count"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // Áp dụng cho tất cả các endpoints
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}
