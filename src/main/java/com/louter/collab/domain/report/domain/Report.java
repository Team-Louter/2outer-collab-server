package com.louter.collab.domain.report.domain;

import com.louter.collab.domain.page.entity.Page;
import com.louter.collab.domain.team.entity.Team;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "reports")
public class Report {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id", nullable = false)
    private Long reportId;

    @Column(name = "text", nullable = false)
    private String text;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @OneToOne
    @JoinColumn(name = "page_id")
    private Page page;

    @ManyToOne
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;
}
