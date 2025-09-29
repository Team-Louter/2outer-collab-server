package com.louter.collab.team.service;

import com.louter.collab.auth.domain.User;
import com.louter.collab.auth.repository.UserRepository;
import com.louter.collab.team.domain.Team;
import com.louter.collab.team.domain.UserTeam;
import com.louter.collab.team.dto.TeamDto;
import com.louter.collab.team.dto.UserTeamDto;
import com.louter.collab.team.mapper.TeamMapper;
import com.louter.collab.team.mapper.UserTeamMapper;
import com.louter.collab.team.repository.TeamRepository;
import com.louter.collab.team.repository.UserTeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TeamService {

    private final TeamRepository teamRepository;
    private final UserTeamRepository userTeamRepository;
    private final UserRepository userRepository;
    private final TeamMapper teamMapper;
    private final UserTeamMapper userTeamMapper;

    @Transactional
    public TeamDto.Response createTeam(TeamDto.CreateRequest request, Long creatorId) {
        // 사용자 존재 여부 확인
        User creator = userRepository.findById(creatorId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        // 팀 생성
        Team team = teamMapper.toEntity(request);
        Team savedTeam = teamRepository.save(team);

        // 팀 생성자를 admin으로 추가
        UserTeam userTeam = UserTeam.builder()
                .userId(creatorId)
                .teamId(savedTeam.getTeamId())
                .role(UserTeam.TeamRole.admin)
                .build();
        userTeamRepository.save(userTeam);

        return teamMapper.toResponseWithUserRole(savedTeam, UserTeam.TeamRole.admin);
    }

    @Transactional
    public UserTeamDto.Response joinTeam(Long teamId, Long userId) {
        // 이미 팀에 속해있는지 확인
        if (userTeamRepository.existsByUserIdAndTeamId(userId, teamId)) {
            throw new RuntimeException("이미 팀에 속해있습니다.");
        }

        // 팀과 사용자 존재 여부 확인
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("팀을 찾을 수 없습니다."));
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        UserTeam userTeam = UserTeam.builder()
                .userId(userId)
                .teamId(teamId)
                .role(UserTeam.TeamRole.user)
                .build();

        UserTeam savedUserTeam = userTeamRepository.save(userTeam);
        
        // 연관 객체 설정 (DTO 변환을 위해)
        savedUserTeam.setUser(user);
        savedUserTeam.setTeam(team);
        
        return userTeamMapper.toResponse(savedUserTeam);
    }

    @Transactional
    public void leaveTeam(Long teamId, Long userId) {
        UserTeam userTeam = userTeamRepository.findByUserIdAndTeamId(userId, teamId)
                .orElseThrow(() -> new RuntimeException("팀 멤버를 찾을 수 없습니다."));

        // admin이 혼자 남은 경우 팀 삭제 또는 다른 처리 로직 추가 가능
        List<UserTeam> admins = userTeamRepository.findAdminsByTeamId(teamId);
        if (admins.size() == 1 && admins.get(0).getUserId().equals(userId)) {
            // 마지막 admin이 탈퇴하는 경우의 처리
            List<UserTeam> allMembers = userTeamRepository.findByTeamId(teamId);
            if (allMembers.size() > 1) {
                throw new RuntimeException("다른 사용자를 admin으로 지정한 후 탈퇴하세요.");
            }
        }

        userTeamRepository.delete(userTeam);
    }

    @Transactional
    public void changeRole(Long teamId, Long userId, UserTeam.TeamRole newRole, Long requesterId) {
        // 요청자가 admin인지 확인
        if (!isUserAdmin(requesterId, teamId)) {
            throw new RuntimeException("권한이 없습니다.");
        }

        UserTeam userTeam = userTeamRepository.findByUserIdAndTeamId(userId, teamId)
                .orElseThrow(() -> new RuntimeException("팀 멤버를 찾을 수 없습니다."));

        userTeam.setRole(newRole);
        userTeamRepository.save(userTeam);
    }

    @Transactional
    public TeamDto.Response updateTeam(Long teamId, TeamDto.UpdateRequest request, Long requesterId) {
        // 요청자가 admin인지 확인
        if (!isUserAdmin(requesterId, teamId)) {
            throw new RuntimeException("권한이 없습니다.");
        }

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("팀을 찾을 수 없습니다."));

        teamMapper.updateEntity(team, request);
        Team updatedTeam = teamRepository.save(team);

        UserTeam.TeamRole userRole = getUserRole(requesterId, teamId);
        return teamMapper.toResponseWithUserRole(updatedTeam, userRole);
    }

    public List<TeamDto.SimpleResponse> getUserTeams(Long userId) {
        List<UserTeam> userTeams = userTeamRepository.findByUserId(userId);
        return userTeams.stream()
                .map(ut -> teamMapper.toSimpleResponse(ut.getTeam(), ut.getRole()))
                .collect(Collectors.toList());
    }

    public List<UserTeamDto.MemberResponse> getTeamMembers(Long teamId) {
        List<UserTeam> members = userTeamRepository.findByTeamId(teamId);
        return userTeamMapper.toMemberResponseList(members);
    }

    public TeamDto.Response getTeamDetails(Long teamId, Long userId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("팀을 찾을 수 없습니다."));

        UserTeam.TeamRole userRole = getUserRole(userId, teamId);
        return teamMapper.toResponseWithUserRole(team, userRole);
    }

    public boolean isUserInTeam(Long userId, Long teamId) {
        return userTeamRepository.existsByUserIdAndTeamId(userId, teamId);
    }

    public boolean isUserAdmin(Long userId, Long teamId) {
        return userTeamRepository.findByUserIdAndTeamId(userId, teamId)
                .map(ut -> ut.getRole() == UserTeam.TeamRole.admin)
                .orElse(false);
    }

    private UserTeam.TeamRole getUserRole(Long userId, Long teamId) {
        return userTeamRepository.findByUserIdAndTeamId(userId, teamId)
                .map(UserTeam::getRole)
                .orElse(null);
    }
}