package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CsrfFilter;


@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/initializeMiTagging")
                )
                .authorizeHttpRequests(authorize -> authorize
                        .anyRequest().permitAll()
                );
        http.addFilterBefore(new CsrfTokenFromPayloadFilter(), CsrfFilter.class);
        return http.build();
    }
}
