package com.louter.collab.team.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JoinRequestActionDto {

    private Long requestId;
    private Boolean approve; // true: 승인, false: 거절
}
