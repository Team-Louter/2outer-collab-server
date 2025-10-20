package com.louter.collab.auth.controller;

import com.louter.collab.auth.dto.request.LoginRequest;
import com.louter.collab.auth.dto.request.SendVerificationEmailRequest;
import com.louter.collab.auth.dto.request.SignupRequest;
import com.louter.collab.auth.repository.UserRepository;
import com.louter.collab.auth.service.AuthService;
import com.louter.collab.auth.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<Object> login(@RequestBody LoginRequest loginRequest) {
        String token = authService.login(loginRequest);
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
