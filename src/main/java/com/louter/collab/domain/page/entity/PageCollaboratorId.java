package com.louter.collab.domain.page.entity;

import jakarta.persistence.Column;

import java.util.Objects;

public class PageCollaboratorId {

    @Column(name = "page_id")
    private Long pageId;

    @Column(name = "user_id")
    private Long userId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PageCollaboratorId that)) return false;
        return Objects.equals(pageId, that.pageId)
                && Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pageId, userId);
    }
}
