package com.louter.collab.team.mapper;

import com.louter.collab.team.domain.Team;
import com.louter.collab.team.domain.UserTeam;
import com.louter.collab.team.dto.TeamDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TeamMapper {

    public TeamDto.Response toResponse(Team team) {
        return TeamDto.Response.builder()
                .teamId(team.getTeamId())
                .teamName(team.getTeamName())
                .profilePicture(team.getProfilePicture())
                .bannerPicture(team.getBannerPicture())
                .intro(team.getIntro())
                .createdAt(team.getCreatedAt())
                .updatedAt(team.getUpdatedAt())
                .memberCount(team.getUserTeams() != null ? team.getUserTeams().size() : 0)
                .build();
    }

    public TeamDto.Response toResponseWithUserRole(Team team, UserTeam.TeamRole userRole) {
        TeamDto.Response response = toResponse(team);
        response.setUserRole(userRole != null ? userRole.name() : null);
        return response;
    }

    public TeamDto.SimpleResponse toSimpleResponse(Team team, UserTeam.TeamRole userRole) {
        return TeamDto.SimpleResponse.builder()
                .teamId(team.getTeamId())
                .teamName(team.getTeamName())
                .profilePicture(team.getProfilePicture())
                .userRole(userRole != null ? userRole.name() : null)
                .build();
    }

    public List<TeamDto.Response> toResponseList(List<Team> teams) {
        return teams.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public Team toEntity(TeamDto.CreateRequest request) {
        return Team.builder()
                .teamName(request.getTeamName())
                .intro(request.getIntro())
                .profilePicture(request.getProfilePicture())
                .bannerPicture(request.getBannerPicture())
                .build();
    }

    public void updateEntity(Team team, TeamDto.UpdateRequest request) {
        team.setTeamName(request.getTeamName());
        team.setIntro(request.getIntro());
        team.setProfilePicture(request.getProfilePicture());
        team.setBannerPicture(request.getBannerPicture());
    }
}