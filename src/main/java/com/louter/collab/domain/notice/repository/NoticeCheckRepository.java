package com.louter.collab.domain.notice.repository;

import com.louter.collab.domain.notice.entity.NoticeCheck;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeCheckRepository extends JpaRepository<NoticeCheck, Long> {
    boolean existsByNoticeNoticeIdAndUserUserId(Long noticeId, Long userId);
}
