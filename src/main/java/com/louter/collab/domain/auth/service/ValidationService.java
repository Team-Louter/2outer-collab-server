package com.louter.collab.domain.auth.service;

import com.louter.collab.domain.auth.dto.request.LoginRequest;
import com.louter.collab.domain.auth.dto.request.SignupRequest;

public interface ValidationService {

    void checkNull(SignupRequest signupRequest);
    void checkNull(LoginRequest loginRequest);
    void checkUserName(String publicId);
    void checkPassword(String password, String confirmPassword);
    void checkEmail(String email);

    void checkExistAccount(String publicId, String email);

}
