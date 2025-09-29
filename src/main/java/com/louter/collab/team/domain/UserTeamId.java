package com.louter.collab.team.domain;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class UserTeamId implements Serializable {
    private Long userId;
    private Long teamId;
}