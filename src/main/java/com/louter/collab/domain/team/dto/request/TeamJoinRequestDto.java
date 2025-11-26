package com.louter.collab.domain.team.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TeamJoinRequestDto {
//
//    @NotNull
//    private Long teamId;

    @NotNull
    private String introduction;

    private String workUrl;
}
