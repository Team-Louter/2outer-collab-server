package com.louter.collab.role.service;

import com.louter.collab.role.domain.Role;

import java.util.List;
import java.util.Set;

public interface RoleService {
    
    /**
     * 팀의 기본 "멤버" 권한 생성
     */
    Role createDefaultMemberRole(Long teamId);
    
    /**
     * 팀의 기본 "관리자" 권한 생성
     */
    Role createDefaultAdminRole(Long teamId);
    
    /**
     * 커스텀 권한 생성
     */
    Role createCustomRole(Long userId, Long teamId, String roleName, Set<String> permissions);
    
    /**
     * 권한 삭제
     */
    void deleteRole(Long userId, Long teamId, Long roleId);
    
    /**
     * 권한에 퍼미션 추가
     */
    void addPermission(Long userId, Long teamId, Long roleId, String permission);
    
    /**
     * 권한에서 퍼미션 제거
     */
    void removePermission(Long userId, Long teamId, Long roleId, String permission);
    
    /**
     * 팀의 모든 권한 조회
     */
    List<Role> getTeamRoles(Long teamId);
    
    /**
     * 특정 권한 조회
     */
    Role getRole(Long roleId);
    
    /**
     * 특정 유저가 특정 팀에서 특정 퍼미션을 가지고 있는지 확인
     */
    boolean hasPermission(Long userId, Long teamId, String permission);
    
    /**
     * 특정 유저가 팀 생성자인지 확인
     */
    boolean isTeamCreator(Long userId, Long teamId);
}
