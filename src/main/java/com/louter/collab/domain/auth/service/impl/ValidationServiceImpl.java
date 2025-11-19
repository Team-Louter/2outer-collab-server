package com.louter.collab.domain.auth.service.impl;

import com.louter.collab.domain.auth.dto.request.LoginRequest;
import com.louter.collab.domain.auth.dto.request.SignupRequest;
import com.louter.collab.domain.auth.repository.UserRepository;
import com.louter.collab.domain.auth.service.ValidationService;
import com.louter.collab.global.common.exception.AlreadyUsingIdException;
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

        if (userName.isEmpty()){
            throw new IllegalArgumentException("닉네임을 입력해주세요");
        } if(userPassword.isEmpty()){
            throw new IllegalArgumentException("비밀번호를 입력해주세요");
        } if(confirmPassword.isEmpty()){
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다");
        } if(userEmail.isEmpty()){
            throw new IllegalArgumentException("이메일을 입력해주세요");
        }
    }

    @Override
    public void checkNull(LoginRequest loginRequest) {

        String userEmail = loginRequest.getUserEmail();
        String userPassword = loginRequest.getUserPassword();
        if (userEmail.isEmpty()){
            throw new IllegalArgumentException("이메일을 입력해주세요");
        } if (userPassword.isEmpty()) {
            throw new IllegalArgumentException("비밀번호를 입력해주세요");
        }
    }

    @Override
    public void checkUserName(String userName) {
        if (userName.length() < 2 || userName.length() > 20) {
            throw new IllegalArgumentException("닉네임 길이가 잘못되었습니다");
        }
        if (!userName.matches("^[a-zA-Z0-9]+$")) {
            throw new IllegalArgumentException("닉네임에 영어, 숫자만 사용해주세요");
        }
        if (userName.matches(".*(.)\\1{2,}.*")) {
            throw new IllegalArgumentException("닉네임은 같은 문자를 3번 이상 연속해서 사용할 수 없습니다.");
        }
        if (!userName.matches("^(?=.*[A-Za-z]).+$")) {
            throw new IllegalArgumentException("닉네임에 영어, 숫자를 포함해주세요");
        }
    }

    @Override
    public void checkPassword(String password, String confirmPassword) {
        if (password.length() < 8 || password.length() > 20) {
            throw new IllegalArgumentException("비밀번호 길이가 잘못되었습니다");
        }
        if (!password.matches("^[A-Za-z0-9@$!%*?&]+$")) {
            throw new IllegalArgumentException("비밀번호에 영어, 숫자, 특수문자만 사용해주세요");
        }
        if (password.matches(".*(.)\\1{2,}.*")) {
            throw new IllegalArgumentException("비밀번호는 같은 문자를 3번 이상 연속해서 사용할 수 없습니다.");
        }
        if (!password.matches("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*?&]).+$")) {
            throw new IllegalArgumentException("비밀번호에 영어, 숫자, 특수문자를 포함해주세요");
        }
        if (!password.equals(confirmPassword)) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
    }


    @Override
    public void checkEmail(String email) {
        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            throw new IllegalArgumentException("잘못된 이메일입니다");
        }
    }

    @Override
    public void checkExistAccount(String userName, String userEmail) {
        if (userRepository.existsByUserEmail(userEmail)) {
            throw new AlreadyUsingIdException("중복된 이메일입니다");
        }
    }
}
