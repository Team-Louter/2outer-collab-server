package com.louter.collab.team.controller;

import com.louter.collab.team.dto.TeamDto;
import com.louter.collab.team.dto.UserTeamDto;
import com.louter.collab.team.service.TeamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teams")
@RequiredArgsConstructor
@Tag(name = "Team Management", description = "팀 관리 API")
public class TeamController {

    private final TeamService teamService;

    @PostMapping
    @Operation(summary = "팀 생성", description = "새로운 팀을 생성합니다. 생성자는 자동으로 admin 권한을 가집니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "팀 생성 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음")
    })
    public ResponseEntity<TeamDto.Response> createTeam(
            @RequestBody TeamDto.CreateRequest request,
            @Parameter(description = "팀 생성자의 사용자 ID", required = true)
            @RequestParam Long userId) {
        TeamDto.Response team = teamService.createTeam(request, userId);
        return ResponseEntity.ok(team);
    }

    @PostMapping("/{teamId}/join")
    @Operation(summary = "팀 가입", description = "기존 팀에 가입합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "팀 가입 성공"),
            @ApiResponse(responseCode = "400", description = "이미 팀에 속해있음"),
            @ApiResponse(responseCode = "404", description = "팀 또는 사용자를 찾을 수 없음")
    })
    public ResponseEntity<UserTeamDto.Response> joinTeam(
            @Parameter(description = "가입할 팀 ID", required = true)
            @PathVariable Long teamId,
            @Parameter(description = "가입할 사용자 ID", required = true)
            @RequestParam Long userId) {
        UserTeamDto.Response userTeam = teamService.joinTeam(teamId, userId);
        return ResponseEntity.ok(userTeam);
    }

    @DeleteMapping("/{teamId}/leave")
    @Operation(summary = "팀 탈퇴", description = "팀에서 탈퇴합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "팀 탈퇴 성공"),
            @ApiResponse(responseCode = "400", description = "마지막 admin은 탈퇴할 수 없음"),
            @ApiResponse(responseCode = "404", description = "팀 멤버를 찾을 수 없음")
    })
    public ResponseEntity<Void> leaveTeam(
            @Parameter(description = "탈퇴할 팀 ID", required = true)
            @PathVariable Long teamId,
            @Parameter(description = "탈퇴할 사용자 ID", required = true)
            @RequestParam Long userId) {
        teamService.leaveTeam(teamId, userId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{teamId}/members/{userId}/role")
    @Operation(summary = "멤버 역할 변경", description = "팀 멤버의 역할을 변경합니다. admin 권한 필요.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "역할 변경 성공"),
            @ApiResponse(responseCode = "403", description = "권한 없음"),
            @ApiResponse(responseCode = "404", description = "팀 멤버를 찾을 수 없음")
    })
    public ResponseEntity<Void> changeRole(
            @Parameter(description = "팀 ID", required = true)
            @PathVariable Long teamId,
            @Parameter(description = "역할을 변경할 사용자 ID", required = true)
            @PathVariable Long userId,
            @RequestBody UserTeamDto.ChangeRoleRequest request,
            @Parameter(description = "요청자 ID (admin 권한 필요)", required = true)
            @RequestParam Long requesterId) {
        teamService.changeRole(teamId, userId, request.getRole(), requesterId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{teamId}")
    @Operation(summary = "팀 정보 수정", description = "팀 정보를 수정합니다. admin 권한 필요.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "팀 정보 수정 성공"),
            @ApiResponse(responseCode = "403", description = "권한 없음"),
            @ApiResponse(responseCode = "404", description = "팀을 찾을 수 없음")
    })
    public ResponseEntity<TeamDto.Response> updateTeam(
            @Parameter(description = "수정할 팀 ID", required = true)
            @PathVariable Long teamId,
            @RequestBody TeamDto.UpdateRequest request,
            @Parameter(description = "요청자 ID (admin 권한 필요)", required = true)
            @RequestParam Long userId) {
        TeamDto.Response team = teamService.updateTeam(teamId, request, userId);
        return ResponseEntity.ok(team);
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "사용자 팀 목록 조회", description = "사용자가 속한 모든 팀을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공")
    public ResponseEntity<List<TeamDto.SimpleResponse>> getUserTeams(
            @Parameter(description = "조회할 사용자 ID", required = true)
            @PathVariable Long userId) {
        List<TeamDto.SimpleResponse> teams = teamService.getUserTeams(userId);
        return ResponseEntity.ok(teams);
    }

    @GetMapping("/{teamId}/members")
    @Operation(summary = "팀 멤버 목록 조회", description = "팀에 속한 모든 멤버를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공")
    public ResponseEntity<List<UserTeamDto.MemberResponse>> getTeamMembers(
            @Parameter(description = "조회할 팀 ID", required = true)
            @PathVariable Long teamId) {
        List<UserTeamDto.MemberResponse> members = teamService.getTeamMembers(teamId);
        return ResponseEntity.ok(members);
    }

    @GetMapping("/{teamId}")
    @Operation(summary = "팀 상세 정보 조회", description = "팀의 상세 정보를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "조회 성공")
    public ResponseEntity<TeamDto.Response> getTeamDetails(
            @Parameter(description = "조회할 팀 ID", required = true)
            @PathVariable Long teamId,
            @Parameter(description = "요청자 사용자 ID", required = true)
            @RequestParam Long userId) {
        TeamDto.Response team = teamService.getTeamDetails(teamId, userId);
        return ResponseEntity.ok(team);
    }

    @GetMapping("/{teamId}/check-membership")
    @Operation(summary = "팀 멤버십 확인", description = "사용자가 특정 팀에 속해있는지 확인합니다.")
    @ApiResponse(responseCode = "200", description = "확인 완료")
    public ResponseEntity<Boolean> checkMembership(
            @Parameter(description = "확인할 팀 ID", required = true)
            @PathVariable Long teamId,
            @Parameter(description = "확인할 사용자 ID", required = true)
            @RequestParam Long userId) {
        boolean isMember = teamService.isUserInTeam(userId, teamId);
        return ResponseEntity.ok(isMember);
    }

    @GetMapping("/{teamId}/check-admin")
    @Operation(summary = "관리자 권한 확인", description = "사용자가 특정 팀의 관리자인지 확인합니다.")
    @ApiResponse(responseCode = "200", description = "확인 완료")
    public ResponseEntity<Boolean> checkAdmin(
            @Parameter(description = "확인할 팀 ID", required = true)
            @PathVariable Long teamId,
            @Parameter(description = "확인할 사용자 ID", required = true)
            @RequestParam Long userId) {
        boolean isAdmin = teamService.isUserAdmin(userId, teamId);
        return ResponseEntity.ok(isAdmin);
    }
}