package com.louter.collab.role.dto.response;

import com.louter.collab.role.domain.Permission;
import com.louter.collab.role.domain.Role;
import com.louter.collab.role.domain.RolePermission;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleResponse {

    private Long roleId;
    private Long teamId;
    private String roleName;
    private String description;
    private Set<Permission> permissions;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static RoleResponse from(Role role) {
        return RoleResponse.builder()
                .roleId(role.getRoleId())
                .teamId(role.getTeam().getTeamId())
                .roleName(role.getRoleName())
                .description(role.getDescription())
                .permissions(role.getPermissions().stream()
                        .map(RolePermission::getPermission)
                        .collect(Collectors.toSet()))
                .createdAt(role.getCreatedAt())
                .updatedAt(role.getUpdatedAt())
                .build();
    }
}