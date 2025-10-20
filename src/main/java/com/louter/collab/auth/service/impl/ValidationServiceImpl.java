package com.louter.collab.auth.service.impl;

import com.louter.collab.auth.dto.request.LoginRequest;
import com.louter.collab.auth.dto.request.SignupRequest;
import com.louter.collab.auth.repository.UserRepository;
import com.louter.collab.auth.service.ValidationService;
import com.louter.collab.common.exception.AlreadyUsingIdException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ValidationServiceImpl implements ValidationService {

    private final UserRepository userRepository;

    @Override
    public void checkNull(SignupRequest signupRequest) {
        String userName = signupRequest.getUserName();
        String userPassword = signupRequest.getUserPassword();
        String confirmPassword = signupRequest.getConfirmPassword();
        String userEmail = signupRequest.getUserEmail();

        if (userName.isEmpty() || userPassword.isEmpty() || confirmPassword.isEmpty() || userEmail.isEmpty()) {
            throw new IllegalArgumentException("빈 값 존재");
        }
    }

    @Override
    public void checkNull(LoginRequest loginRequest) {

        String userEmail = loginRequest.getUserEmail();
        String userPassword = loginRequest.getUserPassword();
        if (userEmail.isEmpty() || userPassword.isEmpty()) {
            throw new IllegalArgumentException("빈 값 존재");
        }
    }

    @Override
    public void checkUserName(String userName) {
        if (userName.length() < 4 || userName.length() > 20) {
            throw new IllegalArgumentException("길이가 잘못됨");
        }
        if (!userName.matches("^[a-zA-Z0-9]+$")) {
            throw new IllegalArgumentException("영어, 숫자 이외의 글자");
        }
        if (userName.matches(".*(.)\\1{2,}.*")) {
            throw new IllegalArgumentException("3글자 이상 연속");
        }
        if (!userName.matches("^(?=.*[A-Za-z])(?=.*\\d).+$")) {
            throw new IllegalArgumentException("영어, 숫자 포함 안 됨");
        }
    }

    @Override
    public void checkPassword(String password, String confirmPassword) {
        if (password.length() < 6 || password.length() > 20) {
            throw new IllegalArgumentException("길이가 잘못됨");
        }
        if (!password.matches("^[A-Za-z0-9@$!%*?&]+$")) {
            throw new IllegalArgumentException("영어, 숫자, 특수문자 이외의 글자");
        }
        if (password.matches(".*(.)\\1{2,}.*")) {
            throw new IllegalArgumentException("3글자 이상 연속");
        }
        if (!password.matches("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*?&]).+$")) {
            throw new IllegalArgumentException("영어, 숫자, 특수문자를 포함 안 됨");
        }
        if (!password.equals(confirmPassword)) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
    }


    @Override
    public void checkEmail(String email) {
        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            throw new IllegalArgumentException("이메일 오류");
        }
    }

    @Override
    public void checkExistAccount(String userName, String userEmail) {
        if (userRepository.existsByUserName(userName) ||
                userRepository.existsByUserEmail(userEmail)) {
            throw new AlreadyUsingIdException("이미 존재함");
        }
    }
}
