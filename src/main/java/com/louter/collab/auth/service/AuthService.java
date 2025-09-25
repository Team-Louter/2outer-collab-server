package com.louter.collab.auth.service;

import com.louter.collab.auth.dto.LoginRequest;
import com.louter.collab.auth.dto.SignupRequest;

public interface AuthService {
    void signup(SignupRequest signupRequest);
    String login(String userAgent, LoginRequest loginRequest);
}
