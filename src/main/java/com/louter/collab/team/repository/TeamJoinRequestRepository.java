package com.louter.collab.team.repository;

import com.louter.collab.team.domain.TeamJoinRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TeamJoinRequestRepository extends JpaRepository<TeamJoinRequest, Long> {
    
    // 특정 팀의 대기 중인 가입 신청 목록
    List<TeamJoinRequest> findByTeam_TeamIdAndStatus(Long teamId, TeamJoinRequest.RequestStatus status);
    
    // 특정 유저의 특정 팀에 대한 대기 중인 신청 확인
    Optional<TeamJoinRequest> findByUser_UserIdAndTeam_TeamIdAndStatus(
            Long userId, Long teamId, TeamJoinRequest.RequestStatus status);
    
    // 특정 유저가 특정 팀에 신청한 적이 있는지 확인
    boolean existsByUser_UserIdAndTeam_TeamIdAndStatus(
            Long userId, Long teamId, TeamJoinRequest.RequestStatus status);
}
