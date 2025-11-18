package com.louter.collab.page.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "page_changes")
@Builder
public class PageChange {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "change_id", nullable = false)
    private Long id;

    @Column(name = "change_content", nullable = false)
    private String changeContent;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

}
