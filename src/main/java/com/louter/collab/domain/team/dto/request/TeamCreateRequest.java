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
public class TeamCreateRequest {

    @NotNull
    private String teamName;
    private String profilePicture;
    private String bannerPicture;
    private String intro;
}
