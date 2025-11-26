package com.louter.collab.domain.team.repository;

import com.louter.collab.domain.team.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TeamRepository extends JpaRepository<Team, Long> {
    Optional<Team> findByTeamName(String teamName);
    Optional<Team> findByTeamId(Long teamId);
    boolean existsByTeamName(String teamName);

    @Query(value = "SELECT * FROM teams t WHERE t.team_id NOT IN (SELECT ut.team_id FROM users_teams ut WHERE ut.user_id = :userId) ORDER BY RAND() LIMIT 18", nativeQuery = true)
    List<Team> findRandomTeamsNotJoinedByUser(Long userId);
}
