package com.louter.collab.auth.service;

import com.louter.collab.auth.dto.request.LoginRequest;
import com.louter.collab.auth.dto.request.SignupRequest;

public interface ValidationService {

    void checkNull(SignupRequest signupRequest);
    void checkNull(LoginRequest loginRequest);
    void checkUserName(String publicId);
    void checkPassword(String password, String confirmPassword);
    void checkEmail(String email);

    void checkExistAccount(String publicId, String email);

}
