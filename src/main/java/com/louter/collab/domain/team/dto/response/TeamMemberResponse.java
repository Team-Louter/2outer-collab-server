package com.louter.collab.domain.team.dto.response;

import com.louter.collab.domain.team.entity.UserTeam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeamMemberResponse {

    private Long userId;
    private String userName;
    private String userEmail;
    private Long roleId;
    private String roleName;

    public static TeamMemberResponse from(UserTeam userTeam) {
        return TeamMemberResponse.builder()
                .userId(userTeam.getUser().getUserId())
                .userName(userTeam.getUser().getUserName())
                .userEmail(userTeam.getUser().getUserEmail())
                .roleId(userTeam.getRole() != null ? userTeam.getRole().getRoleId() : null)
                .roleName(userTeam.getRole() != null ? userTeam.getRole().getRoleName() : null)
                .build();
    }
}
