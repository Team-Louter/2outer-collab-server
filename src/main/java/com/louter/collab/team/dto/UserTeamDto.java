package com.louter.collab.team.dto;

import com.louter.collab.team.domain.UserTeam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

public class UserTeamDto {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "사용자-팀 관계 응답")
    public static class Response {
        @Schema(description = "사용자 ID", example = "1")
        private Long userId;
        
        @Schema(description = "팀 ID", example = "1")
        private Long teamId;
        
        @Schema(description = "사용자명", example = "홍길동")
        private String userName;
        
        @Schema(description = "사용자 이메일", example = "hong@example.com")
        private String userEmail;
        
        @Schema(description = "팀명", example = "개발팀")
        private String teamName;
        
        @Schema(description = "역할", example = "admin", allowableValues = {"admin", "user"})
        private UserTeam.TeamRole role;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "역할 변경 요청")
    public static class ChangeRoleRequest {
        @Schema(description = "새로운 역할", example = "admin", allowableValues = {"admin", "user"}, required = true)
        private UserTeam.TeamRole role;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "팀 멤버 정보")
    public static class MemberResponse {
        @Schema(description = "사용자 ID", example = "1")
        private Long userId;
        
        @Schema(description = "사용자명", example = "홍길동")
        private String userName;
        
        @Schema(description = "사용자 이메일", example = "hong@example.com")
        private String userEmail;
        
        @Schema(description = "역할", example = "user", allowableValues = {"admin", "user"})
        private UserTeam.TeamRole role;
    }
}