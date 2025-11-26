package com.louter.collab.domain.profile.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectInfoResponse {
    private Long teamId;
    private String projectPicture;
}
