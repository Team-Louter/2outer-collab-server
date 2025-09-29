package com.louter.collab.team.domain;

import com.louter.collab.auth.domain.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users_teams")
@IdClass(UserTeamId.class)
public class UserTeam {

    @Id
    @Column(name = "user_id")
    private Long userId;

    @Id
    @Column(name = "team_id")
    private Long teamId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", insertable = false, updatable = false)
    private Team team;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private TeamRole role;

    public enum TeamRole {
        admin, user
    }
}