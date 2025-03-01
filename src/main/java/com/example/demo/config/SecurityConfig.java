package com.example.demo.config;

import jakarta.servlet.Filter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRequestHandler;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfFilter;

import java.util.function.Supplier;

@Configuration
public class SecurityConfig {

    @Bean
    public HttpSessionCsrfTokenRepository csrfTokenRepository() {
        HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
        repository.setHeaderName("X-CSRF-TOKEN");
        return repository;
    }

    // Custom request handler that always resolves the token from the header.
    @Bean
    public CsrfTokenRequestHandler csrfTokenRequestHandler() {
        return new CsrfTokenRequestHandler() {
            @Override
            public void handle(HttpServletRequest request, HttpServletResponse response, Supplier<CsrfToken> csrfToken) {
                request.setAttribute("org.springframework.security.web.csrf.CsrfToken", csrfToken);
            }

            @Override
            public String resolveCsrfTokenValue(HttpServletRequest request, org.springframework.security.web.csrf.CsrfToken csrfToken) {
                // Always read the token from the header.
                return request.getHeader("X-CSRF-TOKEN");
            }
        };
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf
                        .csrfTokenRepository(csrfTokenRepository())
                        .csrfTokenRequestHandler(csrfTokenRequestHandler())
                        .ignoringRequestMatchers("/initializeMiTagging")
                )
                .authorizeHttpRequests(authorize -> authorize
                        .anyRequest().permitAll()
                );
        // Add our custom filter that extracts the token from JSON and injects it as a header.
        http.addFilterBefore(new CsrfTokenFromPayloadFilter(csrfTokenRepository()), CsrfFilter.class);
        return http.build();
    }
}
