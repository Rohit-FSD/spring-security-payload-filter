package com.example.demo.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @PostMapping("/initializeMiTagging")
    public ResponseEntity<Map<String, String>> initializeMiTagging(HttpServletRequest request,
                                                                   HttpServletResponse response) {
        CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
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
