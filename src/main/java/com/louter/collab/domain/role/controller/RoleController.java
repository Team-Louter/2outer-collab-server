package com.louter.collab.domain.role.controller;

import com.louter.collab.domain.auth.jwt.JwtTokenProvider;
import com.louter.collab.domain.role.dto.request.RoleCreateRequest;
import com.louter.collab.domain.role.dto.request.RoleUpdateRequest;
import com.louter.collab.domain.role.dto.response.RoleResponse;
import com.louter.collab.domain.role.service.RoleService;
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

    // 현재 로그인한 사용자 ID 가져오기
    private Long getCurrentUserId() {
        return jwtTokenProvider.getCurrentUserId();
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
                request.getRoleName(), request.getDescription(), request.getPermissions());
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

    // 권한 수정
    @PutMapping("/{roleId}")
    public ResponseEntity<RoleResponse> updateRole(
            @PathVariable Long teamId,
            @PathVariable Long roleId,
            @RequestBody RoleUpdateRequest request) {
        Long userId = getCurrentUserId();
        var role = roleService.updateRole(userId, teamId, roleId, 
                request.getRoleName(), request.getDescription(), request.getPermissions());
        return ResponseEntity.ok(RoleResponse.from(role));
    }
}
