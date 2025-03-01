package com.example.demo.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.filter.OncePerRequestFilter;

public class CsrfTokenFromPayloadFilter extends OncePerRequestFilter {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        if ((request.getMethod().matches("POST|PUT|DELETE"))
                && request.getContentType() != null
                && request.getContentType().contains(MediaType.APPLICATION_JSON_VALUE)) {

            CustomCsrfRequestWrapper wrappedRequest = new CustomCsrfRequestWrapper(request);
            String body = new String(wrappedRequest.getCachedBody(), StandardCharsets.UTF_8);
            String csrfTokenFromBody = extractCsrfTokenFromBody(body);
            if (csrfTokenFromBody != null) {
                csrfTokenFromBody = csrfTokenFromBody.trim();
                System.out.println("Extracted csrfToken from payload: " + csrfTokenFromBody);
                wrappedRequest.addHeader("X-CSRF-TOKEN", csrfTokenFromBody);
            }
//            CsrfToken tokenFromSession = (CsrfToken) request.getSession().getAttribute("csrfToken");
//            if (tokenFromSession != null) {
//                System.out.println("Session CSRF token from session: " + tokenFromSession.getToken());
//                wrappedRequest.setAttribute(CsrfToken.class.getName(), tokenFromSession);
//            }
            System.out.println("Wrapped request header X-CSRF-TOKEN: " + wrappedRequest.getHeader("X-CSRF-TOKEN"));
            filterChain.doFilter(wrappedRequest, response);
        } else {
            filterChain.doFilter(request, response);
        }
    }

    private String extractCsrfTokenFromBody(String body) {
        try {
            JsonNode root = mapper.readTree(body);
            JsonNode tokenNode = root.get("csrfToken");
            if (tokenNode != null) {
                return tokenNode.asText();
            }
        } catch (Exception e) {
            System.out.println("Error parsing JSON in CsrfTokenFromPayloadFilter: " + e);
        }
        return null;
    }
}
