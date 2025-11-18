package com.louter.collab.auth.dto.response;

import com.louter.collab.auth.domain.User;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SendVerificationEmailResponse {
    @NotNull
    private Boolean success;
    @NotNull
    private String userEmail;

    public static SendVerificationEmailResponse from(User user) {
        return SendVerificationEmailResponse.builder()
                .success(true)
                .userEmail(user.getUserEmail())
                .build();
    }
}
