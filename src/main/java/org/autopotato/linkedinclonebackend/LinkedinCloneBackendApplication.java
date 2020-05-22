package org.autopotato.linkedinclonebackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class LinkedinCloneBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(LinkedinCloneBackendApplication.class, args);
    }

    /**
     * Configure CORS settings to all mappings and headers. Allow only GET and POST requests and
     * requests from localhost:4200.
     *
     * @return WebMvcConfigurer with the necessary settings
     */
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {

            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry
                    .addMapping("/**")
                    .allowedOrigins("http://localhost:4200")
                    .allowedMethods("GET", "POST")
                    .allowedHeaders("*");
            }
        };
    }
}
