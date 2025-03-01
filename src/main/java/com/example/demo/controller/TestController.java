package com.example.demo.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    private final HttpSessionCsrfTokenRepository csrfTokenRepository;

    @Autowired
    public TestController(HttpSessionCsrfTokenRepository csrfTokenRepository) {
        this.csrfTokenRepository = csrfTokenRepository;
    }

    @PostMapping("/initializeMiTagging")
    public ResponseEntity<Map<String, String>> initializeMiTagging(HttpServletRequest request,
                                                                   HttpServletResponse response) {
        CsrfToken csrfToken = csrfTokenRepository.loadToken(request);
        if (csrfToken == null) {
            csrfToken = csrfTokenRepository.generateToken(request);
            csrfTokenRepository.saveToken(csrfToken, request, response);
        }
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("csrfToken", csrfToken.getToken());
        return ResponseEntity.ok(responseBody);
    }

    @PostMapping("/verifyUser")
    public ResponseEntity<String> verify(@RequestBody String inputJson, HttpServletRequest request) {
        System.out.println("Inside verifyUser endpoint");
        return ResponseEntity.ok("responseBody");
    }
}
