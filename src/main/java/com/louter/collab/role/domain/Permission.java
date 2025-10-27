package com.louter.collab.role.domain;

public enum Permission {
    TEAM_SETTINGS("팀 관련 설정 편집"),
    ANNOUNCEMENT("공지사항 공지"),
    MEETING_SCHEDULE("회의록/일정 편집"),
    TEAM_CHAT("팀 채팅");

    private final String description;

    Permission(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
