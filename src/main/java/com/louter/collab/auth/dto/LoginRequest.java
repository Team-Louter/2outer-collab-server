package com.louter.collab.auth.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {
    @NotNull
    private String userEmail;

    @NotNull
    private String userPassword;
}
