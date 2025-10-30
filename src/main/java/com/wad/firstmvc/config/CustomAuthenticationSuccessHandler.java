package com.wad.firstmvc.config;

import com.wad.firstmvc.domain.Role;
import com.wad.firstmvc.domain.User;
import com.wad.firstmvc.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import jakarta.servlet.http.*;
import java.io.IOException;


@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        String username = authentication.getName();
        User user = userRepository.findByUsername(username).orElse(null);

        if (user != null) {
            Role role = user.getRole();
            switch (role) {
                case ADMIN -> response.sendRedirect("/admin/dashboard");
                case ADOPTER -> response.sendRedirect("/");
                default -> response.sendRedirect("/login?error");
            }
        } else {
            response.sendRedirect("/login?error");
        }
    }
}


