package com.louter.collab.auth.service.impl;

import com.louter.collab.auth.dto.LoginRequest;
import com.louter.collab.auth.dto.SignupRequest;
import com.louter.collab.auth.repository.UserRepository;
import com.louter.collab.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    @Override
    public void signup(SignupRequest signupRequest) {

    }

    @Override
    public String login(String userAgent, LoginRequest loginRequest) {
        return "";
    }
}
