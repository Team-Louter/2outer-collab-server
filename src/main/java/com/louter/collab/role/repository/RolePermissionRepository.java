package com.louter.collab.role.repository;

import com.louter.collab.role.domain.RolePermission;
import com.louter.collab.role.domain.RolePermissionId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RolePermissionRepository extends JpaRepository<RolePermission, RolePermissionId> {
    
    // 특정 권한의 모든 퍼미션 조회
    List<RolePermission> findByRole_RoleId(Long roleId);
    
    // 특정 권한에 특정 퍼미션이 있는지 확인
    boolean existsByRole_RoleIdAndId_Permission(Long roleId, String permission);
    
    // 특정 권한의 모든 퍼미션 삭제
    void deleteByRole_RoleId(Long roleId);
}
