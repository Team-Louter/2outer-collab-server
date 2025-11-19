package com.louter.collab.page.repository;

import com.louter.collab.page.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PageRepository extends JpaRepository<Page, Long> {
}
