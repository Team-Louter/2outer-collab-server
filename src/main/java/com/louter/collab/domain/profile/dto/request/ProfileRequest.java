package com.louter.collab.domain.profile.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProfileRequest {
    @NotNull
    private Long userId;

    @NotNull
    private String profileImageUrl;

    @NotNull
    private String bio;
}
