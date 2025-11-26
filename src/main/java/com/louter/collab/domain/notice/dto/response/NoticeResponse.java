package com.louter.collab.domain.notice.dto.response;

import com.louter.collab.domain.notice.entity.Notice;
import com.louter.collab.domain.todo.dto.response.TodoResponse;
import com.louter.collab.domain.todo.entity.Todo;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class NoticeResponse {
    private Long noticeId;
    private String title;
    private String content;
    private Boolean check;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static NoticeResponse from(Notice notice) {
        return NoticeResponse.builder()
                .noticeId(notice.getNoticeId())
                .title(notice.getTitle())
                .content(notice.getContent())
                .check(notice.getCheck())
                .createdAt(notice.getCreatedAt())
                .updatedAt(notice.getUpdatedAt())
                .build();
    }
}
