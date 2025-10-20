package com.louter.collab.auth.controller;

import com.louter.collab.auth.dto.request.LoginRequest;
import com.louter.collab.auth.dto.request.SignupRequest;
import com.louter.collab.auth.repository.UserRepository;
import com.louter.collab.auth.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final UserRepository userRepository;

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<Object> signup(@RequestBody SignupRequest signupRequest) {
        authService.signup(signupRequest);
        return ResponseEntity.ok(Map.of("success", true, "userEmail", signupRequest.getUserEmail()));
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody LoginRequest loginRequest) {
        String token = authService.login(loginRequest);
        return ResponseEntity.ok(Map.of("success", true, "token", token));
    }
}
