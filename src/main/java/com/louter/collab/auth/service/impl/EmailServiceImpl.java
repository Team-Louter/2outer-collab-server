package com.louter.collab.auth.service.impl;

import com.louter.collab.auth.dto.request.SendVerificationEmailRequest;
import com.louter.collab.auth.service.EmailService;
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
            message.setSubject("[Collab 이메일 인증 코드]");
            message.setText("인증코드: " + code);

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
