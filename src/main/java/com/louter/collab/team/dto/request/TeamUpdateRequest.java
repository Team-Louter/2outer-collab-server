package com.louter.collab.team.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TeamUpdateRequest {

    private String teamName;
    private String profilePicture;
    private String bannerPicture;
    private String intro;
}
