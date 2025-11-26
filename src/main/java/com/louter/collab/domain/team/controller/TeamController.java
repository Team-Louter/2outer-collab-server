package com.louter.collab.domain.team.controller;

import com.louter.collab.domain.auth.jwt.JwtTokenProvider;
import com.louter.collab.domain.profile.entity.Profile;
import com.louter.collab.domain.profile.repository.ProfileRepository;
import com.louter.collab.domain.team.entity.Team;
import com.louter.collab.domain.team.dto.request.*;
import com.louter.collab.domain.team.dto.response.TeamJoinRequestResponse;
import com.louter.collab.domain.team.dto.response.TeamMemberResponse;
import com.louter.collab.domain.team.dto.response.TeamResponse;
import com.louter.collab.domain.team.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/teams")
public class TeamController {

    private final TeamService teamService;
    private final JwtTokenProvider jwtTokenProvider;
    private final ProfileRepository profileRepository;

    // 현재 로그인한 사용자 ID 가져오기 (토큰 기반, 불필요한 DB 조회 제거)
    private Long getCurrentUserId() {
        return jwtTokenProvider.getCurrentUserId();
    }



    // 팀 생성
    @PostMapping
    public ResponseEntity<TeamResponse> createTeam(@RequestBody TeamCreateRequest request) {
        
        Long userId = getCurrentUserId();
        
        Team team = teamService.createTeam(userId, request.getTeamName(), 
                request.getProfilePicture(), request.getBannerPicture(), request.getIntro());
        List<Long> chatRoomIds = teamService.getChatRoomIds(team.getTeamId());
        return ResponseEntity.ok(TeamResponse.from(team, chatRoomIds));
    }

    // 랜덤 팀 목록 조회
    @GetMapping("/random")
    public ResponseEntity<List<TeamResponse>> getRandomTeams() {
        Long userId = getCurrentUserId();
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        List<Team> teams = teamService.getRandomTeams(userId);

        List<TeamResponse> responses = teams.stream()
                .map(team -> {
                    List<Long> chatRoomIds = teamService.getChatRoomIds(team.getTeamId());
                    return TeamResponse.from(team, chatRoomIds);
                })
                .toList();

        return ResponseEntity.ok(responses);
    }

    // 팀 단건 조회
    @GetMapping("/{teamId}")
    public ResponseEntity<?> getTeam(@PathVariable String teamId) {
        if ("random".equals(teamId)) {
            return getRandomTeams();
        }

        if (!teamId.matches("\\d+")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid team ID");
        }

        Long id = Long.parseLong(teamId);
        Team team = teamService.getTeam(id);
        List<Long> chatRoomIds = teamService.getChatRoomIds(id);
        return ResponseEntity.ok(TeamResponse.from(team, chatRoomIds));
    }

    // 팀 수정
    @PutMapping("/{teamId}")
    public ResponseEntity<TeamResponse> updateTeam(
            @PathVariable Long teamId,
            @RequestBody TeamUpdateRequest request) {
        
        Long userId = getCurrentUserId();
        
        Team team = teamService.updateTeam(userId, teamId, request.getTeamName(), 
                request.getProfilePicture(), request.getBannerPicture(), request.getIntro());
        List<Long> chatRoomIds = teamService.getChatRoomIds(teamId);
        return ResponseEntity.ok(TeamResponse.from(team, chatRoomIds));
    }

    // 팀 삭제
    @DeleteMapping("/{teamId}")
    public ResponseEntity<?> deleteTeam(
            @PathVariable Long teamId,
            @RequestParam String confirmTeamName) {
        Long userId = getCurrentUserId();
        teamService.deleteTeam(userId, teamId, confirmTeamName);
        return ResponseEntity.ok(Map.of("success", true, "message", "팀이 삭제되었습니다."));
    }

    // 팀 가입 신청
    @PostMapping("/{teamId}/join-request")
    public ResponseEntity<TeamJoinRequestResponse> requestJoinTeam(
            @PathVariable("teamId") Long teamId,
            @RequestBody TeamJoinRequestDto request) {
        Long userId = getCurrentUserId();
        var joinRequest = teamService.requestJoinTeam(userId, teamId, request.getIntroduction(),request.getWorkUrl());
        return ResponseEntity.ok(TeamJoinRequestResponse.from(joinRequest));
    }

    // 팀 가입 신청 승인/거절
    @PostMapping("/{teamId}/join-request/process")
    public ResponseEntity<?> processJoinRequest(
            @PathVariable Long teamId,
            @RequestBody JoinRequestActionDto request) {
        Long adminUserId = getCurrentUserId();
        teamService.processJoinRequest(adminUserId, request.getRequestId(), request.getApprove());
        String message = request.getApprove() ? "가입 신청이 승인되었습니다." : "가입 신청이 거절되었습니다.";
        return ResponseEntity.ok(Map.of("success", true, "message", message));
    }

    // 팀의 대기 중인 가입 신청 목록
    @GetMapping("/{teamId}/join-requests")
    public ResponseEntity<List<TeamJoinRequestResponse>> getPendingJoinRequests(@PathVariable Long teamId) {
        var requests = teamService.getPendingJoinRequests(teamId);
        var responses = requests.stream()
                .map(TeamJoinRequestResponse::from)
                .toList();
        return ResponseEntity.ok(responses);
    }

    // 팀 탈퇴
    @DeleteMapping("/{teamId}/leave")
    public ResponseEntity<?> leaveTeam(@PathVariable Long teamId) {
        Long userId = getCurrentUserId();
        teamService.leaveTeam(userId, teamId);
        return ResponseEntity.ok(Map.of("success", true, "message", "팀에서 탈퇴했습니다."));
    }

    // 팀원 추방
    @DeleteMapping("/{teamId}/kick")
    public ResponseEntity<?> kickMember(
            @PathVariable Long teamId,
            @RequestBody TeamKickRequest request) {
        Long adminUserId = getCurrentUserId();
        teamService.kickMember(adminUserId, teamId, request.getTargetUserId());
        return ResponseEntity.ok(Map.of("success", true, "message", "팀원이 추방되었습니다."));
    }

    // 내 팀 목록 조회
    @GetMapping("/my-teams")
    public ResponseEntity<List<TeamResponse>> getUserTeams() {
        Long userId = getCurrentUserId();
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        List<Team> teams = teamService.getUserTeams(userId);
        List<TeamResponse> responses = teams.stream()
                .map(team -> {
                    List<Long> chatRoomIds = teamService.getChatRoomIds(team.getTeamId());
                    return TeamResponse.from(team, chatRoomIds);
                })
                .toList();
        return ResponseEntity.ok(responses);
    }

    // 팀 멤버 목록
    @GetMapping("/{teamId}/members")
    public ResponseEntity<List<TeamMemberResponse>> getTeamMembers(@PathVariable Long teamId) {
        var members = teamService.getTeamMembers(teamId);

        List<Long> userIds = members.stream()
                .map(m -> m.getUser().getUserId())
                .toList();

        Map<Long, String> profileImages = new java.util.HashMap<>();
        profileRepository.findAllById(userIds).forEach(p ->
                profileImages.put(p.getUserId(), p.getProfileImageUrl())
        );

        var responses = members.stream()
                .map(member -> TeamMemberResponse.from(member, profileImages.get(member.getUser().getUserId())))
                .toList();
        return ResponseEntity.ok(responses);
    }

    // 팀 멤버 권한 변경
    @PutMapping("/{teamId}/members/role")
    public ResponseEntity<?> changeMemberRole(
            @PathVariable Long teamId,
            @RequestBody ChangeMemberRoleRequest request) {
        Long adminUserId = getCurrentUserId();
        teamService.changeMemberRole(adminUserId, teamId, request.getTargetUserId(), request.getNewRoleId());
        return ResponseEntity.ok(Map.of("success", true, "message", "멤버의 권한이 변경되었습니다."));
    }
}
