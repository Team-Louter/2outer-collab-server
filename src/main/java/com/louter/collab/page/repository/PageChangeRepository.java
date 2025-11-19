package com.louter.collab.page.repository;

import com.louter.collab.page.domain.PageChange;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PageChangeRepository extends JpaRepository<PageChange, Long> {
}
