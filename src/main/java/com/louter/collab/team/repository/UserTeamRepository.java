package com.louter.collab.team.repository;

import com.louter.collab.team.domain.UserTeam;
import com.louter.collab.team.domain.UserTeamId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserTeamRepository extends JpaRepository<UserTeam, UserTeamId> {
    
    // 특정 유저가 속한 팀 목록
    List<UserTeam> findByUser_UserId(Long userId);
    
    // 특정 팀의 멤버 목록
    List<UserTeam> findByTeam_TeamId(Long teamId);
    
    // 특정 유저가 특정 팀에 속해있는지 확인
    boolean existsByUser_UserIdAndTeam_TeamId(Long userId, Long teamId);
    
    // 특정 유저의 특정 팀에서의 권한 조회
    @Query("SELECT ut FROM UserTeam ut WHERE ut.user.userId = :userId AND ut.team.teamId = :teamId")
    Optional<UserTeam> findByUserIdAndTeamId(Long userId, Long teamId);
    
    // 특정 팀에서 특정 권한을 가진 멤버들
    List<UserTeam> findByTeam_TeamIdAndRole_RoleId(Long teamId, Long roleId);
}
