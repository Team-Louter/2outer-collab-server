package com.louter.collab.team.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

public class TeamDto {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "팀 응답 정보")
    public static class Response {
        @Schema(description = "팀 ID", example = "1")
        private Long teamId;
        
        @Schema(description = "팀 이름", example = "개발팀")
        private String teamName;
        
        @Schema(description = "프로필 사진 URL", example = "https://example.com/profile.jpg")
        private String profilePicture;
        
        @Schema(description = "배너 사진 URL", example = "https://example.com/banner.jpg")
        private String bannerPicture;
        
        @Schema(description = "팀 소개", example = "우리는 최고의 개발팀입니다")
        private String intro;
        
        @Schema(description = "생성일시")
        private LocalDateTime createdAt;
        
        @Schema(description = "수정일시")
        private LocalDateTime updatedAt;
        
        @Schema(description = "멤버 수", example = "5")
        private Integer memberCount;
        
        @Schema(description = "현재 사용자의 역할", example = "admin", allowableValues = {"admin", "user"})
        private String userRole;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "팀 생성 요청")
    public static class CreateRequest {
        @Schema(description = "팀 이름", example = "개발팀", required = true)
        private String teamName;
        
        @Schema(description = "팀 소개", example = "우리는 최고의 개발팀입니다")
        private String intro;
        
        @Schema(description = "프로필 사진 URL", example = "https://example.com/profile.jpg")
        private String profilePicture;
        
        @Schema(description = "배너 사진 URL", example = "https://example.com/banner.jpg")
        private String bannerPicture;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "팀 수정 요청")
    public static class UpdateRequest {
        @Schema(description = "팀 이름", example = "수정된 개발팀")
        private String teamName;
        
        @Schema(description = "팀 소개", example = "수정된 팀 소개")
        private String intro;
        
        @Schema(description = "프로필 사진 URL", example = "https://example.com/new_profile.jpg")
        private String profilePicture;
        
        @Schema(description = "배너 사진 URL", example = "https://example.com/new_banner.jpg")
        private String bannerPicture;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Schema(description = "팀 간단 정보")
    public static class SimpleResponse {
        @Schema(description = "팀 ID", example = "1")
        private Long teamId;
        
        @Schema(description = "팀 이름", example = "개발팀")
        private String teamName;
        
        @Schema(description = "프로필 사진 URL", example = "https://example.com/profile.jpg")
        private String profilePicture;
        
        @Schema(description = "현재 사용자의 역할", example = "admin", allowableValues = {"admin", "user"})
        private String userRole;
    }
}