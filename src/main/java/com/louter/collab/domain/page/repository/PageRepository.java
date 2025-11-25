package com.louter.collab.domain.page.repository;

import com.louter.collab.domain.page.entity.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PageRepository extends JpaRepository<Page, Long> {
    List<Page> findByTeamTeamId(Long teamId);
}
