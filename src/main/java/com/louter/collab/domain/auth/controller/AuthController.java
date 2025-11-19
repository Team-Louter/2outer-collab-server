package com.louter.collab.domain.auth.controller;

import com.louter.collab.domain.auth.dto.request.LoginRequest;
import com.louter.collab.domain.auth.dto.request.SendVerificationEmailRequest;
import com.louter.collab.domain.auth.dto.request.SignupRequest;
import com.louter.collab.domain.auth.repository.UserRepository;
import com.louter.collab.domain.auth.service.AuthService;
import com.louter.collab.domain.auth.service.EmailService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final UserRepository userRepository;
    private final EmailService emailService;

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<Object> signup(@RequestBody SignupRequest signupRequest) {
        authService.signup(signupRequest);
        return ResponseEntity.ok(Map.of("success", true, "userEmail", signupRequest.getUserEmail()));
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        String token = authService.login(loginRequest);

        ResponseCookie cookie = ResponseCookie.from("token", token)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(24 * 60 * 60)
                .sameSite("None")
                .build();

        response.setHeader(org.springframework.http.HttpHeaders.SET_COOKIE, cookie.toString());

        return ResponseEntity.ok(Map.of("success", true, "token", token));
    }

    // 이메일 전송
    @GetMapping("/email")
    public ResponseEntity<String> sendVerificationCode(
            @RequestParam("userEmail") String userEmail) {
        try {
            SendVerificationEmailRequest emailRequest = new SendVerificationEmailRequest();
            emailRequest.setUserEmail(userEmail);

            emailService.sendVerificationEmail(emailRequest);

            return ResponseEntity.ok("전송 완료");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("전송 실패: " + e.getMessage());
        }
    }


    // 이메일 인증
    @GetMapping("/verify")
    public ResponseEntity<String> verifyCode(
            @RequestParam("userEmail") String userEmail,
            @RequestParam("inputCode") String inputCode) {
        if (emailService.verifyEmail(userEmail, inputCode)) {
            return ResponseEntity.ok("인증 성공");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("인증 실패");
        }
    }

}
