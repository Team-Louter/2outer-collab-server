package com.louter.collab.domain.page.repository;

import com.louter.collab.domain.page.entity.Page;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PageRepository extends JpaRepository<Page, Long> {
    Long id(Long id);
}
