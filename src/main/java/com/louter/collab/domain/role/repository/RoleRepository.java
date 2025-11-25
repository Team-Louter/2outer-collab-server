package com.louter.collab.domain.role.repository;

import com.louter.collab.domain.role.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    
    // 특정 팀의 모든 권한 조회
    List<Role> findByTeam_TeamId(Long teamId);
    
    // 특정 팀에서 특정 이름의 권한 조회
    Optional<Role> findByTeam_TeamIdAndRoleName(Long teamId, String roleName);
    
    // 특정 팀에 특정 권한 이름이 존재하는지 확인
    boolean existsByTeam_TeamIdAndRoleName(Long teamId, String roleName);
    
    // 특정 팀의 모든 권한 삭제
    void deleteByTeam_TeamId(Long teamId);
}
