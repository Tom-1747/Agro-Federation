package com.example.agricultureFederation.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebConfig {

    private final Dotenv dotenv = Dotenv.configure()
            .directory("./")
            .ignoreIfMissing()
            .load();

    @Bean
    public FilterRegistrationBean<ApiKeyFilter> apiKeyFilter() {
        String apiKey = dotenv.get("API_KEY", "agri-secure-key");

        FilterRegistrationBean<ApiKeyFilter> registration =
                new FilterRegistrationBean<>();

        registration.setFilter(new ApiKeyFilter(apiKey));
        registration.addUrlPatterns("/*");
        registration.setOrder(1);

        return registration;
    }
}