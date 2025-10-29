package com.louter.collab.team.service.impl;

import com.louter.collab.auth.domain.User;
import com.louter.collab.auth.repository.UserRepository;
import com.louter.collab.common.exception.IllegalArgumentException;
import com.louter.collab.common.exception.UserNotFoundException;
import com.louter.collab.role.domain.Permission;
import com.louter.collab.role.domain.Role;
import com.louter.collab.role.service.RoleService;
import com.louter.collab.team.domain.Team;
import com.louter.collab.team.domain.TeamJoinRequest;
import com.louter.collab.team.domain.UserTeam;
import com.louter.collab.team.domain.UserTeamId;
import com.louter.collab.team.repository.TeamJoinRequestRepository;
import com.louter.collab.team.repository.TeamRepository;
import com.louter.collab.team.repository.UserTeamRepository;
import com.louter.collab.team.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;
    private final UserTeamRepository userTeamRepository;
    private final TeamJoinRequestRepository teamJoinRequestRepository;
    private final UserRepository userRepository;
    private final RoleService roleService;

    @Override
    @Transactional
    public Team createTeam(Long creatorId, String teamName, String profilePicture, String bannerPicture, String intro) {
        // 유저 존재 확인
        User creator = userRepository.findById(creatorId)
                .orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다."));

        // 팀 이름 중복 확인
        if (teamRepository.existsByTeamName(teamName)) {
            throw new IllegalArgumentException("이미 존재하는 팀 이름입니다.");
        }

        // 팀 생성
        Team team = Team.builder()
                .teamName(teamName)
                .creator(creator)
                .profilePicture(profilePicture)
                .bannerPicture(bannerPicture)
                .intro(intro)
                .build();
        team = teamRepository.save(team);

        // 기본 권한 생성 (멤버, 관리자)
        roleService.createDefaultMemberRole(team.getTeamId());
        Role adminRole = roleService.createDefaultAdminRole(team.getTeamId());

        // 생성자를 팀에 추가 (관리자 권한 부여)
        UserTeamId userTeamId = new UserTeamId(creatorId, team.getTeamId());
        UserTeam userTeam = UserTeam.builder()
                .id(userTeamId)
                .user(creator)
                .team(team)
                .role(adminRole)
                .build();
        userTeamRepository.save(userTeam);

        return team;
    }

    @Override
    @Transactional
    public void deleteTeam(Long userId, Long teamId, String confirmTeamName) {
        // 팀 존재 확인
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("팀을 찾을 수 없습니다."));

        // 생성자 확인
        if (!team.getCreator().getUserId().equals(userId)) {
            throw new IllegalArgumentException("팀 생성자만 팀을 삭제할 수 있습니다.");
        }

        // 팀 이름 확인
        if (!team.getTeamName().equals(confirmTeamName)) {
            throw new IllegalArgumentException("팀 이름이 일치하지 않습니다.");
        }

        // 연관 데이터 삭제
        // 1. 팀-유저 관계 삭제
        userTeamRepository.deleteByTeam_TeamId(teamId);
        
        // 2. 팀 가입 신청 삭제
        teamJoinRequestRepository.deleteByTeam_TeamId(teamId);
        
        // 3. 팀 권한 삭제 (RolePermission은 cascade로 삭제됨)
        roleService.deleteTeamRoles(teamId);
        
        // 4. 팀 삭제
        teamRepository.delete(team);
    }

    @Override
    @Transactional
    public TeamJoinRequest requestJoinTeam(Long userId, Long teamId) {
        // 유저 존재 확인
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다."));

        // 팀 존재 확인
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("팀을 찾을 수 없습니다."));

        // 이미 가입되어 있는지 확인
        if (userTeamRepository.existsByUser_UserIdAndTeam_TeamId(userId, teamId)) {
            throw new IllegalArgumentException("이미 가입된 팀입니다.");
        }

        // 이미 대기 중인 신청이 있는지 확인
        if (teamJoinRequestRepository.existsByUser_UserIdAndTeam_TeamIdAndStatus(
                userId, teamId, TeamJoinRequest.RequestStatus.PENDING)) {
            throw new IllegalArgumentException("이미 가입 신청이 대기 중입니다.");
        }

        // 가입 신청 생성
        TeamJoinRequest joinRequest = TeamJoinRequest.builder()
                .user(user)
                .team(team)
                .status(TeamJoinRequest.RequestStatus.PENDING)
                .build();

        return teamJoinRequestRepository.save(joinRequest);
    }

    @Override
    @Transactional
    public void processJoinRequest(Long adminUserId, Long requestId, boolean approve) {
        // 가입 신청 존재 확인
        TeamJoinRequest joinRequest = teamJoinRequestRepository.findById(requestId)
                .orElseThrow(() -> new IllegalArgumentException("가입 신청을 찾을 수 없습니다."));

        // 관리자 권한 확인 (TEAM_SETTINGS 퍼미션 필요)
        if (!roleService.hasPermission(adminUserId, joinRequest.getTeam().getTeamId(), Permission.TEAM_SETTINGS) &&
            !roleService.isTeamCreator(adminUserId, joinRequest.getTeam().getTeamId())) {
            throw new IllegalArgumentException("가입 신청을 처리할 권한이 없습니다.");
        }

        // 이미 처리된 신청인지 확인
        if (joinRequest.getStatus() != TeamJoinRequest.RequestStatus.PENDING) {
            throw new IllegalArgumentException("이미 처리된 가입 신청입니다.");
        }

        // 관리자 정보 조회
        User admin = userRepository.findById(adminUserId)
                .orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다."));

        if (approve) {
            // 승인: 팀에 추가
            // 기본 "멤버" 권한 조회
            Role memberRole = roleService.getTeamRoles(joinRequest.getTeam().getTeamId()).stream()
                    .filter(role -> role.getRoleName().equals("멤버"))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("기본 멤버 권한을 찾을 수 없습니다."));

            // 팀 가입 처리
            UserTeamId userTeamId = new UserTeamId(
                    joinRequest.getUser().getUserId(),
                    joinRequest.getTeam().getTeamId()
            );
            UserTeam userTeam = UserTeam.builder()
                    .id(userTeamId)
                    .user(joinRequest.getUser())
                    .team(joinRequest.getTeam())
                    .role(memberRole)
                    .build();
            userTeamRepository.save(userTeam);

            joinRequest.setStatus(TeamJoinRequest.RequestStatus.APPROVED);
        } else {
            // 거절
            joinRequest.setStatus(TeamJoinRequest.RequestStatus.REJECTED);
        }

        joinRequest.setProcessedAt(java.time.LocalDateTime.now());
        joinRequest.setProcessedBy(admin);
        teamJoinRequestRepository.save(joinRequest);
    }

    @Override
    public List<TeamJoinRequest> getPendingJoinRequests(Long teamId) {
        // 팀 존재 확인
        if (!teamRepository.existsById(teamId)) {
            throw new IllegalArgumentException("팀을 찾을 수 없습니다.");
        }

        return teamJoinRequestRepository.findByTeam_TeamIdAndStatus(
                teamId, TeamJoinRequest.RequestStatus.PENDING);
    }

    @Override
    @Transactional
    public void leaveTeam(Long userId, Long teamId) {
        // 팀 존재 확인
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("팀을 찾을 수 없습니다."));

        // 생성자는 탈퇴 불가
        if (team.getCreator().getUserId().equals(userId)) {
            throw new IllegalArgumentException("팀 생성자는 탈퇴할 수 없습니다. 팀을 삭제해주세요.");
        }

        // 가입 여부 확인
        UserTeamId userTeamId = new UserTeamId(userId, teamId);
        UserTeam userTeam = userTeamRepository.findById(userTeamId)
                .orElseThrow(() -> new IllegalArgumentException("가입되지 않은 팀입니다."));

        // 팀 탈퇴
        userTeamRepository.delete(userTeam);
    }

    @Override
    @Transactional
    public void kickMember(Long adminUserId, Long teamId, Long targetUserId) {
        // 관리자 권한 확인 (TEAM_SETTINGS 퍼미션 필요)
        if (!roleService.hasPermission(adminUserId, teamId, Permission.TEAM_SETTINGS)) {
            throw new IllegalArgumentException("팀원을 추방할 권한이 없습니다.");
        }

        // 팀 존재 확인
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("팀을 찾을 수 없습니다."));

        // 생성자는 추방 불가
        if (team.getCreator().getUserId().equals(targetUserId)) {
            throw new IllegalArgumentException("팀 생성자는 추방할 수 없습니다.");
        }

        // 자기 자신 추방 불가
        if (adminUserId.equals(targetUserId)) {
            throw new IllegalArgumentException("자기 자신을 추방할 수 없습니다.");
        }

        // 대상 유저 가입 여부 확인
        UserTeamId userTeamId = new UserTeamId(targetUserId, teamId);
        UserTeam userTeam = userTeamRepository.findById(userTeamId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저는 팀에 가입되어 있지 않습니다."));

        // 추방
        userTeamRepository.delete(userTeam);
    }

    @Override
    public Team getTeam(Long teamId) {
        return teamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("팀을 찾을 수 없습니다."));
    }

    @Override
    public List<Team> getUserTeams(Long userId) {
        List<UserTeam> userTeams = userTeamRepository.findByUser_UserId(userId);
        return userTeams.stream()
                .map(UserTeam::getTeam)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserTeam> getTeamMembers(Long teamId) {
        // 팀 존재 확인
        if (!teamRepository.existsById(teamId)) {
            throw new IllegalArgumentException("팀을 찾을 수 없습니다.");
        }

        return userTeamRepository.findByTeam_TeamId(teamId);
    }

    @Override
    @Transactional
    public void changeMemberRole(Long adminUserId, Long teamId, Long targetUserId, Long newRoleId) {
        // 팀 존재 확인
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("팀을 찾을 수 없습니다."));

        // 관리자 권한 확인 (TEAM_SETTINGS 퍼미션 또는 생성자)
        if (!roleService.hasPermission(adminUserId, teamId, Permission.TEAM_SETTINGS) &&
            !roleService.isTeamCreator(adminUserId, teamId)) {
            throw new IllegalArgumentException("멤버 권한을 변경할 권한이 없습니다.");
        }

        // 대상 유저가 팀에 속해있는지 확인
        UserTeamId userTeamId = new UserTeamId(targetUserId, teamId);
        UserTeam userTeam = userTeamRepository.findById(userTeamId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저는 팀에 속해있지 않습니다."));

        // 새 권한 존재 확인
        Role newRole = roleService.getRole(newRoleId);
        
        // 새 권한이 해당 팀의 권한인지 확인
        if (!newRole.getTeam().getTeamId().equals(teamId)) {
            throw new IllegalArgumentException("해당 권한은 이 팀의 권한이 아닙니다.");
        }

        // 팀 생성자의 권한은 변경 불가
        if (team.getCreator().getUserId().equals(targetUserId)) {
            throw new IllegalArgumentException("팀 생성자의 권한은 변경할 수 없습니다.");
        }

        // 권한 변경
        userTeam.setRole(newRole);
        userTeamRepository.save(userTeam);
    }

    @Override
    @Transactional
    public Team updateTeam(Long userId, Long teamId, String teamName, String profilePicture, String bannerPicture, String intro) {
        // 팀 존재 확인
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("팀을 찾을 수 없습니다."));

        // 생성자 확인
        if (!team.getCreator().getUserId().equals(userId)) {
            throw new IllegalArgumentException("팀 생성자만 팀 정보를 수정할 수 있습니다.");
        }

        // 팀 이름 변경 시 중복 확인
        if (teamName != null && !teamName.equals(team.getTeamName())) {
            if (teamRepository.existsByTeamName(teamName)) {
                throw new IllegalArgumentException("이미 존재하는 팀 이름입니다.");
            }
            team.setTeamName(teamName);
        }

        // 정보 수정
        if (profilePicture != null) {
            team.setProfilePicture(profilePicture);
        }
        if (bannerPicture != null) {
            team.setBannerPicture(bannerPicture);
        }
        if (intro != null) {
            team.setIntro(intro);
        }

        return teamRepository.save(team);
    }
}
