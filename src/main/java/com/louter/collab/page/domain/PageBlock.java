package com.louter.collab.page.domain;

import com.louter.collab.auth.domain.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "page_blocks")
@Builder
public class PageBlock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "block_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "page_id",nullable = false)
    private Page page;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_block_id")
    private PageBlock parent;

    @Column(name = "content", nullable = false)
    private String content;

    @Column(name = "type", length = 50, nullable = false)
    private String type;

    @Column(name = "order_index", nullable = false)
    private Integer orderIndex;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    public void update(String content, String type, Integer orderIndex) {
        this.content = content;
        this.type = type;
        this.orderIndex = orderIndex;
    }

}
