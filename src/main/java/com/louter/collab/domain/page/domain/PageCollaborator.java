package com.louter.collab.domain.page.domain;

import com.louter.collab.auth.domain.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Fetch;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "page_collaborators")
public class PageCollaborator {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "page_id", nullable = false)
    private Page page;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

}
