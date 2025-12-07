package com.purrfect.adoption_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

@FeignClient(name = "auth-service", url = "${auth-service.url:http://localhost:8081}")
public interface AuthServiceClient {
    @GetMapping("/auth/users/{id}")
    Map<String, Object> getUserById(@PathVariable Long id);
}




