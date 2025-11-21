package com.louter.collab.domain.profile.dto.response;

import com.louter.collab.domain.profile.entity.Profile;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
public class ProfileResponse {
    @NotNull
    private Long userId;

    @NotNull
    private String userName;

    @NotNull
    private String profileImageUrl;

    private List<String> projects;
    private String bio;

    @NotNull
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static ProfileResponse of(Profile profile, String userName, List<String> projects) {
        return ProfileResponse.builder()
                .userId(profile.getUserId())
                .userName(userName)
                .profileImageUrl(profile.getProfileImageUrl())
                .projects(projects)
                .bio(profile.getBio())
                .createdAt(profile.getCreatedAt())
                .updatedAt(profile.getUpdatedAt())
                .build();
    }
}
