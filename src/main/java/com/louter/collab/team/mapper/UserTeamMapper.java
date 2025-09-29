package com.louter.collab.team.mapper;

import com.louter.collab.team.domain.UserTeam;
import com.louter.collab.team.dto.UserTeamDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserTeamMapper {

    public UserTeamDto.Response toResponse(UserTeam userTeam) {
        return UserTeamDto.Response.builder()
                .userId(userTeam.getUserId())
                .teamId(userTeam.getTeamId())
                .userName(userTeam.getUser() != null ? userTeam.getUser().getUserName() : null)
                .userEmail(userTeam.getUser() != null ? userTeam.getUser().getUserEmail() : null)
                .teamName(userTeam.getTeam() != null ? userTeam.getTeam().getTeamName() : null)
                .role(userTeam.getRole())
                .build();
    }

    public UserTeamDto.MemberResponse toMemberResponse(UserTeam userTeam) {
        return UserTeamDto.MemberResponse.builder()
                .userId(userTeam.getUserId())
                .userName(userTeam.getUser() != null ? userTeam.getUser().getUserName() : null)
                .userEmail(userTeam.getUser() != null ? userTeam.getUser().getUserEmail() : null)
                .role(userTeam.getRole())
                .build();
    }

    public List<UserTeamDto.Response> toResponseList(List<UserTeam> userTeams) {
        return userTeams.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<UserTeamDto.MemberResponse> toMemberResponseList(List<UserTeam> userTeams) {
        return userTeams.stream()
                .map(this::toMemberResponse)
                .collect(Collectors.toList());
    }
}