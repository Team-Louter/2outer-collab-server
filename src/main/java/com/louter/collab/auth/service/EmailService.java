package com.louter.collab.auth.service;

import com.louter.collab.auth.dto.request.SendVerificationEmailRequest;

public interface EmailService {
    String sendVerificationEmail(SendVerificationEmailRequest request);
    Boolean verifyEmail(String email, String inputCode);
}
