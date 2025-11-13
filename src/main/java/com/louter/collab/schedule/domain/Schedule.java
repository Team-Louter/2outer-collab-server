package com.louter.collab.schedule.domain;

import com.louter.collab.team.domain.Team;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "schedules")
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_id", nullable = false)
    private Long scheduleId;

    @Column(name = "schedule_content", nullable = false)
    private String scheduleContent;

    @Column(name = "schedule_title", nullable = false)
    private String scheduleTitle;

    @Enumerated(EnumType.STRING)
    @Column(name = "schedule_color", nullable = false)
    @Builder.Default
    private Color scheduleColor = Color.LightGrey;

    @Column(name = "schedule_date", nullable = false)
    private LocalDateTime scheduleDate;

    @ManyToOne
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;
}
