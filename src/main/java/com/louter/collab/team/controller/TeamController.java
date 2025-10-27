package com.louter.collab.team.controller;

import com.louter.collab.team.domain.Team;
import com.louter.collab.team.dto.request.*;
import com.louter.collab.team.dto.response.TeamJoinRequestResponse;
import com.louter.collab.team.dto.response.TeamMemberResponse;
import com.louter.collab.team.dto.response.TeamResponse;
import com.louter.collab.team.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/teams")
public class TeamController {

    private final TeamService teamService;

    // 팀 생성
    @PostMapping
    public ResponseEntity<TeamResponse> createTeam(
            @RequestParam Long userId,  // 임시: JWT 구현 전까지 파라미터로 받음
            @RequestBody TeamCreateRequest request) {
        Team team = teamService.createTeam(userId, request.getTeamName(), 
                request.getProfilePicture(), request.getBannerPicture(), request.getIntro());
        return ResponseEntity.ok(TeamResponse.from(team));
    }

    // 팀 조회
    @GetMapping("/{teamId}")
    public ResponseEntity<TeamResponse> getTeam(@PathVariable Long teamId) {
        Team team = teamService.getTeam(teamId);
        return ResponseEntity.ok(TeamResponse.from(team));
    }

    // 팀 수정
    @PutMapping("/{teamId}")
    public ResponseEntity<TeamResponse> updateTeam(
            @PathVariable Long teamId,
            @RequestParam Long userId,  // 임시: JWT 구현 전까지 파라미터로 받음
            @RequestBody TeamUpdateRequest request) {
        Team team = teamService.updateTeam(userId, teamId, request.getTeamName(), 
                request.getProfilePicture(), request.getBannerPicture(), request.getIntro());
        return ResponseEntity.ok(TeamResponse.from(team));
    }

    // 팀 삭제
    @DeleteMapping("/{teamId}")
    public ResponseEntity<Object> deleteTeam(
            @PathVariable Long teamId,
            @RequestParam Long userId,  // 임시: JWT 구현 전까지 파라미터로 받음
            @RequestBody TeamDeleteRequest request) {
        teamService.deleteTeam(userId, teamId, request.getConfirmTeamName());
        return ResponseEntity.ok(Map.of("success", true, "message", "팀이 삭제되었습니다."));
    }

    // 팀 가입 신청
    @PostMapping("/{teamId}/join-request")
    public ResponseEntity<TeamJoinRequestResponse> requestJoinTeam(
            @PathVariable Long teamId,
            @RequestParam Long userId) {  // 임시: JWT 구현 전까지 파라미터로 받음
        var joinRequest = teamService.requestJoinTeam(userId, teamId);
        return ResponseEntity.ok(TeamJoinRequestResponse.from(joinRequest));
    }

    // 팀 가입 신청 승인/거절
    @PostMapping("/{teamId}/join-request/process")
    public ResponseEntity<Object> processJoinRequest(
            @PathVariable Long teamId,
            @RequestParam Long adminUserId,  // 임시: JWT 구현 전까지 파라미터로 받음
            @RequestBody JoinRequestActionDto request) {
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
    public ResponseEntity<Object> leaveTeam(
            @PathVariable Long teamId,
            @RequestParam Long userId) {  // 임시: JWT 구현 전까지 파라미터로 받음
        teamService.leaveTeam(userId, teamId);
        return ResponseEntity.ok(Map.of("success", true, "message", "팀에서 탈퇴했습니다."));
    }

    // 팀원 추방
    @DeleteMapping("/{teamId}/kick")
    public ResponseEntity<Object> kickMember(
            @PathVariable Long teamId,
            @RequestParam Long adminUserId,  // 임시: JWT 구현 전까지 파라미터로 받음
            @RequestBody TeamKickRequest request) {
        teamService.kickMember(adminUserId, teamId, request.getTargetUserId());
        return ResponseEntity.ok(Map.of("success", true, "message", "팀원이 추방되었습니다."));
    }

    // 내가 속한 팀 목록
    @GetMapping("/my-teams")
    public ResponseEntity<List<TeamResponse>> getMyTeams(@RequestParam Long userId) {  // 임시
        var teams = teamService.getUserTeams(userId);
        var responses = teams.stream()
                .map(TeamResponse::from)
                .toList();
        return ResponseEntity.ok(responses);
    }

    // 팀 멤버 목록
    @GetMapping("/{teamId}/members")
    public ResponseEntity<List<TeamMemberResponse>> getTeamMembers(@PathVariable Long teamId) {
        var members = teamService.getTeamMembers(teamId);
        var responses = members.stream()
                .map(TeamMemberResponse::from)
                .toList();
        return ResponseEntity.ok(responses);
    }

    // 팀 멤버 권한 변경
    @PutMapping("/{teamId}/members/role")
    public ResponseEntity<Object> changeMemberRole(
            @PathVariable Long teamId,
            @RequestParam Long adminUserId,  // 임시: JWT 구현 전까지 파라미터로 받음
            @RequestBody ChangeMemberRoleRequest request) {
        teamService.changeMemberRole(adminUserId, teamId, request.getTargetUserId(), request.getNewRoleId());
        return ResponseEntity.ok(Map.of("success", true, "message", "멤버의 권한이 변경되었습니다."));
    }
}
