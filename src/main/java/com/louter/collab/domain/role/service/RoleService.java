package com.louter.collab.domain.role.service;

import com.louter.collab.domain.role.domain.Permission;
import com.louter.collab.domain.role.domain.Role;

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
    Role createCustomRole(Long userId, Long teamId, String roleName, String description, Set<Permission> permissions);
    
    /**
     * 권한 삭제
     */
    void deleteRole(Long userId, Long teamId, Long roleId);
    
    /**
     * 권한 수정 (이름, 설명, 퍼미션 목록)
     */
    Role updateRole(Long userId, Long teamId, Long roleId, String roleName, String description, Set<Permission> permissions);
    
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
    boolean hasPermission(Long userId, Long teamId, Permission permission);
    
    /**
     * 특정 유저가 팀 생성자인지 확인
     */
    boolean isTeamCreator(Long userId, Long teamId);
    
    /**
     * 팀의 모든 권한 삭제
     */
    void deleteTeamRoles(Long teamId);
}
