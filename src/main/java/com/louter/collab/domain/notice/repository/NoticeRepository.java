package com.louter.collab.domain.notice.repository;

import com.mysql.cj.protocol.x.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
}
