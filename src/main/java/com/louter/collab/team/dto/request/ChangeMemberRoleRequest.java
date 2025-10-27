package com.louter.collab.team.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChangeMemberRoleRequest {

    private Long targetUserId;
    private Long newRoleId;
}
