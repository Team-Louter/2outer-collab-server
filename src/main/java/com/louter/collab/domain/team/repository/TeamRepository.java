package com.louter.collab.domain.team.repository;

import com.louter.collab.domain.team.domain.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TeamRepository extends JpaRepository<Team, Long> {
    Optional<Team> findByTeamName(String teamName);
    Optional<Team> findByTeamId(Long teamId);
    boolean existsByTeamName(String teamName);
}
