package com.louter.collab.domain.page.repository;

import com.louter.collab.page.domain.PageCollaborator;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PageCollaboratorRepository extends JpaRepository<PageCollaborator, Long> {
}
