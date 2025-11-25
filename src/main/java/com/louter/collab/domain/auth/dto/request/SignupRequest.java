package com.louter.collab.domain.auth.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequest {
    @NotNull
    private String userName;

    @NotNull
    private String userPassword;

    @NotNull
    private String confirmPassword;

    @NotNull
    private String userEmail;
}
