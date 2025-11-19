package com.louter.collab.page.repository;

import com.louter.collab.page.domain.PageCollaborator;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PageCollaboratorRepository extends JpaRepository<PageCollaborator, Long> {
}
