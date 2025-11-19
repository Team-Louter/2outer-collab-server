package com.louter.collab.role.domain;

public enum Permission {
    // 팀 설정
    TEAM_SETTINGS("팀 관련 설정 편집"),
    
    // 공지사항
    ANNOUNCEMENT("공지사항 공지"),
    
    // 일정
    SCHEDULE("일정 편집"),

    // 회의록
    MEETING_MINUTES("회의록 편집"),

    // 채널 관리
    MANAGE_CHANNELS("채널 생성/삭제/편집"),

    @Deprecated
    MEETING_SCHEDULE("회의록/일정 편집 (구버전)"),

    @Deprecated
    TEAM_CHAT("팀 채팅 (구버전)");

    private final String description;

    Permission(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
