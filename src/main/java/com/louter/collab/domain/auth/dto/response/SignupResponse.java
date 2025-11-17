package com.louter.collab.domain.auth.dto.response;

import com.louter.collab.domain.auth.domain.User;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

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
