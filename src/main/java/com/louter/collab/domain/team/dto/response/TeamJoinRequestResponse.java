package com.louter.collab.domain.team.dto.response;

import com.louter.collab.domain.team.domain.TeamJoinRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeamJoinRequestResponse {

    private Long requestId;
    private Long userId;
    private String userName;
    private String userEmail;
    private Long teamId;
    private String teamName;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime processedAt;
    private String processedByName;

    public static TeamJoinRequestResponse from(TeamJoinRequest request) {
        return TeamJoinRequestResponse.builder()
                .requestId(request.getRequestId())
                .userId(request.getUser().getUserId())
                .userName(request.getUser().getUserName())
                .userEmail(request.getUser().getUserEmail())
                .teamId(request.getTeam().getTeamId())
                .teamName(request.getTeam().getTeamName())
                .status(request.getStatus().name())
                .createdAt(request.getCreatedAt())
                .processedAt(request.getProcessedAt())
                .processedByName(request.getProcessedBy() != null ? 
                        request.getProcessedBy().getUserName() : null)
                .build();
    }
}
