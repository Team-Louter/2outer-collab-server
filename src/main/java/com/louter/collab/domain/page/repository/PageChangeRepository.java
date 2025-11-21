package com.louter.collab.domain.page.repository;

import com.louter.collab.domain.page.entity.PageChange;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PageChangeRepository extends JpaRepository<PageChange, Long> {
}
