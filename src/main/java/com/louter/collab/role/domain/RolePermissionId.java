package com.louter.collab.role.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Embeddable
public class RolePermissionId implements Serializable {

    @Column(name = "role_id", nullable = false)
    private Long roleId;

    @Column(name = "permission", nullable = false, length = 50)
    private String permission;
}