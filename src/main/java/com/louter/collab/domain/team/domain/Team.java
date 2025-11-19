package com.louter.collab.domain.team.domain;

import com.louter.collab.domain.auth.domain.User;
import com.louter.collab.chat.domain.ChatRoom;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "teams")
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_id", nullable = false)
    private Long teamId;

    @Column(name = "team_name", nullable = false, length = 255)
    private String teamName;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "creator_id", nullable = false)
    private User creator;

    @Column(name = "profile_picture", length = 255)
    private String profilePicture;

    @Column(name = "banner_picture", length = 255)
    private String bannerPicture;

    @Column(name = "intro", length = 255)
    private String intro;

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChatRoom> chatRooms = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}