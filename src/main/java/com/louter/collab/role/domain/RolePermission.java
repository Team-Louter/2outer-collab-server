package com.louter.collab.role.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "role_permissions")
public class RolePermission {

    @EmbeddedId
    private RolePermissionId id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId("roleId")
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    // 편의 접근자
    public Permission getPermission() {
        return id != null ? id.getPermission() : null;
    }

    public void setPermission(Permission permission) {
        if (this.id == null) this.id = new RolePermissionId();
        this.id.setPermission(permission);
    }
}