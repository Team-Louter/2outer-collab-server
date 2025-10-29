package com.louter.collab.role.controller;

import com.louter.collab.auth.jwt.JwtTokenProvider;
import com.louter.collab.auth.repository.UserRepository;
import com.louter.collab.common.exception.UserNotFoundException;
import com.louter.collab.role.dto.request.PermissionRequest;
import com.louter.collab.role.dto.request.RoleCreateRequest;
import com.louter.collab.role.dto.response.RoleResponse;
import com.louter.collab.role.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/teams/{teamId}/roles")
public class RoleController {

    private final RoleService roleService;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    // 현재 로그인한 사용자 ID 가져오기
    private Long getCurrentUserId() {
        Long userId = jwtTokenProvider.getCurrentUserId();
        return userRepository.findByUserId(userId)
                .orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다."))
                .getUserId();
    }

    // 팀의 모든 권한 조회
    @GetMapping
    public ResponseEntity<List<RoleResponse>> getTeamRoles(@PathVariable Long teamId) {
        var roles = roleService.getTeamRoles(teamId);
        var responses = roles.stream()
                .map(RoleResponse::from)
                .toList();
        return ResponseEntity.ok(responses);
    }

    // 커스텀 권한 생성
    @PostMapping
    public ResponseEntity<RoleResponse> createCustomRole(
            @PathVariable Long teamId,
            @RequestBody RoleCreateRequest request) {
        Long userId = getCurrentUserId();
        var role = roleService.createCustomRole(userId, teamId, 
                request.getRoleName(), request.getPermissions());
        return ResponseEntity.ok(RoleResponse.from(role));
    }

    // 권한 삭제
    @DeleteMapping("/{roleId}")
    public ResponseEntity<?> deleteRole(
            @PathVariable Long teamId,
            @PathVariable Long roleId) {
        Long userId = getCurrentUserId();
        roleService.deleteRole(userId, teamId, roleId);
        return ResponseEntity.ok(Map.of("success", true, "message", "권한이 삭제되었습니다."));
    }

    // 권한에 퍼미션 추가
    @PostMapping("/{roleId}/permissions")
    public ResponseEntity<?> addPermission(
            @PathVariable Long teamId,
            @PathVariable Long roleId,
            @RequestBody PermissionRequest request) {
        Long userId = getCurrentUserId();
        roleService.addPermission(userId, teamId, roleId, request.getPermission());
        return ResponseEntity.ok(Map.of("success", true, "message", "퍼미션이 추가되었습니다."));
    }

    // 권한에서 퍼미션 제거
    @DeleteMapping("/{roleId}/permissions")
    public ResponseEntity<?> removePermission(
            @PathVariable Long teamId,
            @PathVariable Long roleId,
            @RequestBody PermissionRequest request) {
        Long userId = getCurrentUserId();
        roleService.removePermission(userId, teamId, roleId, request.getPermission());
        return ResponseEntity.ok(Map.of("success", true, "message", "퍼미션이 제거되었습니다."));
    }
}
