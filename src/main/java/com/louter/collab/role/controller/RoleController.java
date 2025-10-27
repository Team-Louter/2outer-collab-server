package com.louter.collab.role.controller;

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
            @RequestParam Long userId,  // 임시: JWT 구현 전까지 파라미터로 받음
            @RequestBody RoleCreateRequest request) {
        var role = roleService.createCustomRole(userId, teamId, 
                request.getRoleName(), request.getPermissions());
        return ResponseEntity.ok(RoleResponse.from(role));
    }

    // 권한 삭제
    @DeleteMapping("/{roleId}")
    public ResponseEntity<?> deleteRole(
            @PathVariable Long teamId,
            @PathVariable Long roleId,
            @RequestParam Long userId) {  // 임시: JWT 구현 전까지 파라미터로 받음
        roleService.deleteRole(userId, teamId, roleId);
        return ResponseEntity.ok(Map.of("success", true, "message", "권한이 삭제되었습니다."));
    }

    // 권한에 퍼미션 추가
    @PostMapping("/{roleId}/permissions")
    public ResponseEntity<?> addPermission(
            @PathVariable Long teamId,
            @PathVariable Long roleId,
            @RequestParam Long userId,  // 임시: JWT 구현 전까지 파라미터로 받음
            @RequestBody PermissionRequest request) {
        roleService.addPermission(userId, teamId, roleId, request.getPermission());
        return ResponseEntity.ok(Map.of("success", true, "message", "퍼미션이 추가되었습니다."));
    }

    // 권한에서 퍼미션 제거
    @DeleteMapping("/{roleId}/permissions")
    public ResponseEntity<?> removePermission(
            @PathVariable Long teamId,
            @PathVariable Long roleId,
            @RequestParam Long userId,  // 임시: JWT 구현 전까지 파라미터로 받음
            @RequestBody PermissionRequest request) {
        roleService.removePermission(userId, teamId, roleId, request.getPermission());
        return ResponseEntity.ok(Map.of("success", true, "message", "퍼미션이 제거되었습니다."));
    }
}
