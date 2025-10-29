package com.louter.collab.auth.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
    @NotNull
    private Long userId;

    @NotNull
    private String userEmail;

    @NotNull
    private String userPassword;
}
