package com.louter.collab.team.repository;

import com.louter.collab.team.domain.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {
    
    @Query("SELECT t FROM Team t JOIN t.userTeams ut WHERE ut.userId = :userId")
    List<Team> findTeamsByUserId(@Param("userId") Long userId);
    
    @Query("SELECT t FROM Team t JOIN t.userTeams ut WHERE ut.userId = :userId AND ut.role = 'admin'")
    List<Team> findTeamsByUserIdAndAdminRole(@Param("userId") Long userId);
}