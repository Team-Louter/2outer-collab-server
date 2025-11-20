package com.louter.collab.domain.auth.service.impl;

import com.louter.collab.domain.auth.entity.User;
import com.louter.collab.domain.auth.dto.request.LoginRequest;
import com.louter.collab.domain.auth.dto.request.SignupRequest;
import com.louter.collab.domain.auth.jwt.JwtAuthenticationFilter;
import com.louter.collab.domain.auth.jwt.JwtTokenProvider;
import com.louter.collab.domain.auth.repository.UserRepository;
import com.louter.collab.domain.auth.service.AuthService;
import com.louter.collab.domain.auth.service.ValidationService;
import com.louter.collab.global.common.exception.UserNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final ValidationService validationService;

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    @Override
    public void signup(SignupRequest signupRequest) {
        validationService.checkNull(signupRequest);

        String userName = signupRequest.getUserName();
        String userPassword = signupRequest.getUserPassword();
        String confirmPassword = signupRequest.getConfirmPassword();
        String userEmail = signupRequest.getUserEmail();

        validationService.checkUserName(userName);
        validationService.checkPassword(userPassword, confirmPassword);
        validationService.checkEmail(userEmail);
        validationService.checkExistAccount(userName, userEmail);

        User user = User.builder()
                .userName(userName)
                .userPassword(passwordEncoder.encode(userPassword))
                .userEmail(userEmail)
                .build();

        userRepository.save(user);
    }

    @Override
    public String login(LoginRequest loginRequest) {
        validationService.checkNull(loginRequest);

        String userEmail = loginRequest.getUserEmail();
        String userPassword = loginRequest.getUserPassword();

        Optional<User> tempUser = userRepository.findByUserEmail(userEmail);
        if (tempUser.isEmpty()) {
            throw new UserNotFoundException("유저 조회 실패");
        }

        User user = tempUser.get();
        if (!passwordEncoder.matches(userPassword, user.getUserPassword())) {
            throw new UserNotFoundException("유저 조회 실패");
        }

        return jwtTokenProvider.generateToken(user.getUserId());
    }

    @Override
    public User getUser(String userEmail) {
        return userRepository.findByUserEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException("유저 조회 실패"));
    }
}
