package com.louter.collab.team.dto.response;

import com.louter.collab.team.domain.Team;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

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
    private String teamProfileUrl;
    private List<Long> chatRoomIds;

    public TeamResponse(Long id, String teamName, String teamDescription, String teamProfileUrl, List<Long> chatRoomIds) {
        this.teamId = id;
        this.teamName = teamName;
        this.intro = teamDescription;
        this.teamProfileUrl = teamProfileUrl;
        this.chatRoomIds = chatRoomIds;
    }

    public static TeamResponse from(Team team, List<Long> chatRoomIds) {
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
                .chatRoomIds(chatRoomIds)
                .build();
    }
}
