package com.louter.collab.role.dto.request;

import com.louter.collab.role.domain.Permission;
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
public class RoleUpdateRequest {

    @NotNull
    private String roleName;
    
    private String description;

    @NotNull
    private Set<Permission> permissions;
}
