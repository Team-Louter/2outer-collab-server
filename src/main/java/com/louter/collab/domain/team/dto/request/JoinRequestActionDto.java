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
public class JoinRequestActionDto {
    @NotNull
    private Long requestId;
    @NotNull
    private Boolean approve; // true: 승인, false: 거절
}
