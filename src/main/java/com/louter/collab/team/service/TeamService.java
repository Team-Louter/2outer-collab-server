package com.louter.collab.team.service;

import com.louter.collab.team.domain.Team;
import com.louter.collab.team.domain.TeamJoinRequest;

import java.util.List;

public interface TeamService {
    
    /**
     * 팀 생성 (생성자는 자동으로 팀에 추가되고 기본 관리자 권한 부여)
     */
    Team createTeam(Long creatorId, String teamName, String profilePicture, String bannerPicture, String intro);
    
    /**
     * 팀 삭제 (생성자만 가능, 팀 이름 확인 필수)
     */
    void deleteTeam(Long userId, Long teamId, String confirmTeamName);
    
    /**
     * 팀 가입 신청
     */
    TeamJoinRequest requestJoinTeam(Long userId, Long teamId);
    
    /**
     * 팀 가입 신청 승인/거절 (관리자만 가능)
     */
    void processJoinRequest(Long adminUserId, Long requestId, boolean approve);
    
    /**
     * 팀의 대기 중인 가입 신청 목록 조회
     */
    List<TeamJoinRequest> getPendingJoinRequests(Long teamId);
    
    /**
     * 팀 탈퇴
     */
    void leaveTeam(Long userId, Long teamId);
    
    /**
     * 팀원 추방 (관리자만 가능)
     */
    void kickMember(Long adminUserId, Long teamId, Long targetUserId);
    
    /**
     * 팀 정보 조회
     */
    Team getTeam(Long teamId);
    
    /**
     * 특정 유저가 속한 팀 목록
     */
    List<Team> getUserTeams(Long userId);
    
    /**
     * 팀 멤버 목록 조회
     */
    List<com.louter.collab.team.domain.UserTeam> getTeamMembers(Long teamId);
    
    /**
     * 팀 멤버의 권한 변경 (관리자만 가능)
     */
    void changeMemberRole(Long adminUserId, Long teamId, Long targetUserId, Long newRoleId);
    
    /**
     * 팀 정보 수정 (생성자만 가능)
     */
    Team updateTeam(Long userId, Long teamId, String teamName, String profilePicture, String bannerPicture, String intro);
}
