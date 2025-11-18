package com.louter.collab.auth.dto.response;

import com.louter.collab.auth.domain.User;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.security.Provider;

@Getter
@Setter
@Builder
public class SignupResponse {
    @NotNull
    private Boolean success;
    @NotNull
    private Long userId;
    @NotNull
    private String userEmail;

    public static SignupResponse from(User user) {
        return SignupResponse.builder()
                .success(true)
                .userId(user.getUserId())
                .userEmail(user.getUserEmail())
                .build();
    }

}
