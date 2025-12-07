package com.purrfect.auth_service.dto;

import com.purrfect.auth_service.domain.User;
import lombok.Data;

@Data
public class LoginResponse {
    private String token;
    private User user;
}
