package com.louter.collab.domain.page.entity;

import com.louter.collab.domain.auth.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "page_collaborators")
public class PageCollaborator {

    @EmbeddedId
    private PageCollaboratorId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("pageId")
    @JoinColumn(name = "page_id", nullable = false)
    private Page page;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

}
