package com.louter.collab.domain.auth.service.impl;

import com.louter.collab.domain.auth.dto.request.SendVerificationEmailRequest;
import com.louter.collab.domain.auth.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender mailSender;
    private final Map<String, String> codeMap = new HashMap<>();

    protected String generateCode() {
        return String.format("%06d", new Random().nextInt(1000000));
    }

    @Override
    public String sendVerificationEmail(SendVerificationEmailRequest request) {
        String code = generateCode();
        codeMap.put(request.getUserEmail(), code);

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("ldy2009aa@gmail.com");
            message.setTo(request.getUserEmail());
            message.setSubject("[인증번호: " + code + "] Collab 이메일 인증을 진행해 주세요!");
            message.setText("안녕하세요, Collab 회원님!\n" +
                    "\n" +
                    "Collab 이메일 인증을 위한 코드를 보내드립니다.\n" +
                    "아래 인증 코드를 입력하여 이메일 인증을 완료해주세요.\n" +
                    "\n" +
                    "---------------------------------\n" +
                    "**✨ 인증 코드:  " + code + " ✨**\n" +
                    "---------------------------------\n" +
                    "\n" +
                    "*   이 코드는 보안을 위해 10분 동안 유효합니다.\n" +
                    "*   만약 본인이 요청한 것이 아니라면, 이 메일을 무시해주세요.\n" +
                    "\n" +
                    "감사합니다.\n" +
                    "Collab 팀 드림");

            mailSender.send(message);
            return code;

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("전송 중 오류: " + e.getMessage());
        }
    }

    @Override
    public Boolean verifyEmail(String email, String inputCode) {
        String savedCode = codeMap.get(email);
        return savedCode != null && savedCode.equals(inputCode);
    }
}
