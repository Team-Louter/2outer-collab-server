package com.louter.collab.team.repository;

import com.louter.collab.team.domain.UserTeam;
import com.louter.collab.team.domain.UserTeamId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserTeamRepository extends JpaRepository<UserTeam, UserTeamId> {
    
    @Query("SELECT ut FROM UserTeam ut JOIN FETCH ut.user JOIN FETCH ut.team WHERE ut.userId = :userId")
    List<UserTeam> findByUserId(@Param("userId") Long userId);
    
    @Query("SELECT ut FROM UserTeam ut JOIN FETCH ut.user WHERE ut.teamId = :teamId")
    List<UserTeam> findByTeamId(@Param("teamId") Long teamId);
    
    Optional<UserTeam> findByUserIdAndTeamId(Long userId, Long teamId);
    
    boolean existsByUserIdAndTeamId(Long userId, Long teamId);
    
    @Query("SELECT ut FROM UserTeam ut WHERE ut.teamId = :teamId AND ut.role = 'admin'")
    List<UserTeam> findAdminsByTeamId(@Param("teamId") Long teamId);
}