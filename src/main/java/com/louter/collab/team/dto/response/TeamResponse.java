package com.louter.collab.team.dto.response;

import com.louter.collab.team.domain.Team;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeamResponse {

    private Long teamId;
    private String teamName;
    private Long creatorId;
    private String creatorName;
    private String profilePicture;
    private String bannerPicture;
    private String intro;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static TeamResponse from(Team team) {
        return TeamResponse.builder()
                .teamId(team.getTeamId())
                .teamName(team.getTeamName())
                .creatorId(team.getCreator().getUserId())
                .creatorName(team.getCreator().getUserName())
                .profilePicture(team.getProfilePicture())
                .bannerPicture(team.getBannerPicture())
                .intro(team.getIntro())
                .createdAt(team.getCreatedAt())
                .updatedAt(team.getUpdatedAt())
                .build();
    }
}
