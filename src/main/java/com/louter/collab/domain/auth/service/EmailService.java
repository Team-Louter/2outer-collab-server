package com.louter.collab.domain.auth.service;

import com.louter.collab.domain.auth.dto.request.SendVerificationEmailRequest;

public interface EmailService {
    String sendVerificationEmail(SendVerificationEmailRequest request);
    Boolean verifyEmail(String email, String inputCode);
}
