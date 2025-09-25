package com.louter.collab.auth.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequest {
    @NotNull
    private String userId;

    @NotNull
    private String userPassword;

    @NotNull
    private String confirmPassword;

    // 이메일 인증
}
