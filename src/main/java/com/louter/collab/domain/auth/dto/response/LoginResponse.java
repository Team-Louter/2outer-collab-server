package com.louter.collab.domain.auth.dto.response;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class LoginResponse {
    @NotNull
    private Boolean success;

    @NotNull
    private String token;

    @NotNull
    private Long userId;

    @NotNull
    private String userName;
}
