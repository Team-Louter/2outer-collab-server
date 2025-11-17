package com.louter.collab.domain.auth.service;

import com.louter.collab.domain.auth.dto.request.LoginRequest;
import com.louter.collab.domain.auth.dto.request.SignupRequest;

public interface AuthService {
    void signup(SignupRequest signupRequest);
    String login(LoginRequest loginRequest);
}
