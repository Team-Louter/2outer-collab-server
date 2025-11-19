package com.louter.collab.domain.role.dto.request;

import com.louter.collab.domain.role.entity.Permission;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoleCreateRequest {

    @NotNull
    private String roleName;
    
    private String description;

    @NotNull
    private Set<Permission> permissions;
}
