package com.louter.collab.domain.page.repository;

import com.louter.collab.domain.page.entity.PageCollaborator;
import com.louter.collab.domain.page.entity.PageCollaboratorId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PageCollaboratorRepository extends JpaRepository<PageCollaborator, PageCollaboratorId> {
}
