package com.louter.collab.domain.role.service.impl;

import com.louter.collab.global.common.exception.IllegalArgumentException;
import com.louter.collab.domain.role.domain.Permission;
import com.louter.collab.domain.role.domain.Role;
import com.louter.collab.domain.role.domain.RolePermission;
import com.louter.collab.domain.role.domain.RolePermissionId;
import com.louter.collab.domain.role.repository.RolePermissionRepository;
import com.louter.collab.domain.role.repository.RoleRepository;
import com.louter.collab.domain.role.service.RoleService;
import com.louter.collab.domain.team.domain.Team;
import com.louter.collab.domain.team.domain.UserTeam;
import com.louter.collab.domain.team.repository.TeamRepository;
import com.louter.collab.domain.team.repository.UserTeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final RolePermissionRepository rolePermissionRepository;
    private final TeamRepository teamRepository;
    private final UserTeamRepository userTeamRepository;

    @Override
    @Transactional
    public Role createDefaultMemberRole(Long teamId) {
        // 팀 존재 확인
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("팀을 찾을 수 없습니다."));

        // 이미 멤버 권한이 있는지 확인
        if (roleRepository.existsByTeam_TeamIdAndRoleName(teamId, "멤버")) {
            return roleRepository.findByTeam_TeamIdAndRoleName(teamId, "멤버")
                    .orElseThrow(() -> new IllegalArgumentException("멤버 권한을 찾을 수 없습니다."));
        }

        // 멤버 권한 생성 (권한 없음 - 뷰어만 가능)
        Role memberRole = Role.builder()
                .team(team)
                .roleName("멤버")
                .description("기본 멤버 권한입니다.")
                .permissions(new HashSet<>())
                .build();
        memberRole = roleRepository.save(memberRole);

        // 멤버는 권한 없이 뷰어로만 동작 (커스텀 역할로 권한 추가 가능)

        return memberRole;
    }

    @Override
    @Transactional
    public Role createDefaultAdminRole(Long teamId) {
        // 팀 존재 확인
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("팀을 찾을 수 없습니다."));

        // 이미 관리자 권한이 있는지 확인
        if (roleRepository.existsByTeam_TeamIdAndRoleName(teamId, "관리자")) {
            return roleRepository.findByTeam_TeamIdAndRoleName(teamId, "관리자")
                    .orElseThrow(() -> new IllegalArgumentException("관리자 권한을 찾을 수 없습니다."));
        }

        // 관리자 권한 생성
        Role adminRole = Role.builder()
                .team(team)
                .roleName("관리자")
                .description("팀 관리자 권한입니다.")
                .permissions(new HashSet<>())
                .build();
        adminRole = roleRepository.save(adminRole);

        // 관리자 퍼미션 추가 (모든 권한)
        addPermissionToRole(adminRole, Permission.TEAM_SETTINGS);
        addPermissionToRole(adminRole, Permission.ANNOUNCEMENT);
        addPermissionToRole(adminRole, Permission.SCHEDULE);
        addPermissionToRole(adminRole, Permission.MEETING_MINUTES);

        return adminRole;
    }

    @Override
    @Transactional
    public Role createCustomRole(Long userId, Long teamId, String roleName, String description, Set<Permission> permissions) {
        // TEAM_SETTINGS 권한 확인 (권한 관리)
        if (!hasPermission(userId, teamId, Permission.TEAM_SETTINGS)) {
            throw new IllegalArgumentException("권한을 관리할 권한이 없습니다.");
        }

        // 팀 존재 확인
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("팀을 찾을 수 없습니다."));

        // 권한 이름 중복 확인
        if (roleRepository.existsByTeam_TeamIdAndRoleName(teamId, roleName)) {
            throw new IllegalArgumentException("이미 존재하는 권한 이름입니다.");
        }

        // 커스텀 권한 생성
        Role customRole = Role.builder()
                .team(team)
                .roleName(roleName)
                .description(description)
                .permissions(new HashSet<>())
                .build();
        customRole = roleRepository.save(customRole);

        // 퍼미션 추가
        if (permissions != null && !permissions.isEmpty()) {
            for (Permission permission : permissions) {
                addPermissionToRole(customRole, permission);
            }
        }

        return customRole;
    }

    @Override
    @Transactional
    public void deleteRole(Long userId, Long teamId, Long roleId) {
        // TEAM_SETTINGS 권한 확인
        if (!hasPermission(userId, teamId, Permission.TEAM_SETTINGS)) {
            throw new IllegalArgumentException("권한을 관리할 권한이 없습니다.");
        }

        // 권한 존재 확인
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new IllegalArgumentException("권한을 찾을 수 없습니다."));

        // 해당 팀의 권한인지 확인
        if (!role.getTeam().getTeamId().equals(teamId)) {
            throw new IllegalArgumentException("해당 팀의 권한이 아닙니다.");
        }

        // 기본 권한(멤버, 관리자) 삭제 불가
        if (role.getRoleName().equals("멤버") || role.getRoleName().equals("관리자")) {
            throw new IllegalArgumentException("기본 권한은 삭제할 수 없습니다.");
        }

        // 해당 권한을 사용 중인 유저가 있는지 확인
        List<UserTeam> usersWithRole = userTeamRepository.findByTeam_TeamIdAndRole_RoleId(teamId, roleId);
        if (!usersWithRole.isEmpty()) {
            throw new IllegalArgumentException("해당 권한을 사용 중인 유저가 있습니다.");
        }

        // 권한 삭제
        roleRepository.delete(role);
    }

    @Override
    @Transactional
    public Role updateRole(Long userId, Long teamId, Long roleId, String roleName, String description, Set<Permission> permissions) {
        // TEAM_SETTINGS 권한 확인
        if (!hasPermission(userId, teamId, Permission.TEAM_SETTINGS)) {
            throw new IllegalArgumentException("권한을 관리할 권한이 없습니다.");
        }

        // 권한 존재 확인
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new IllegalArgumentException("권한을 찾을 수 없습니다."));

        // 해당 팀의 권한인지 확인
        if (!role.getTeam().getTeamId().equals(teamId)) {
            throw new IllegalArgumentException("해당 팀의 권한이 아닙니다.");
        }

        // 기본 권한(멤버, 관리자) 수정 불가
        if (role.getRoleName().equals("멤버") || role.getRoleName().equals("관리자")) {
            throw new IllegalArgumentException("기본 권한은 수정할 수 없습니다.");
        }

        // 이름 변경 시 중복 확인
        if (!role.getRoleName().equals(roleName) && roleRepository.existsByTeam_TeamIdAndRoleName(teamId, roleName)) {
            throw new IllegalArgumentException("이미 존재하는 권한 이름입니다.");
        }

        // 정보 업데이트
        role.setRoleName(roleName);
        role.setDescription(description);
        roleRepository.save(role);

        // 기존 퍼미션 삭제
        rolePermissionRepository.deleteByRole_RoleId(roleId);

        // 새 퍼미션 추가
        if (permissions != null && !permissions.isEmpty()) {
            for (Permission permission : permissions) {
                addPermissionToRole(role, permission);
            }
        }
        
        return role;
    }

    @Override
    public List<Role> getTeamRoles(Long teamId) {
        return roleRepository.findByTeam_TeamId(teamId);
    }

    @Override
    public Role getRole(Long roleId) {
        return roleRepository.findById(roleId)
                .orElseThrow(() -> new IllegalArgumentException("권한을 찾을 수 없습니다."));
    }

    @Override
    public boolean hasPermission(Long userId, Long teamId, Permission permission) {
        // 생성자는 모든 권한 보유
        if (isTeamCreator(userId, teamId)) {
            return true;
        }

        // 유저의 팀 멤버십 조회
        UserTeam userTeam = userTeamRepository.findByUserIdAndTeamId(userId, teamId)
                .orElse(null);

        if (userTeam == null || userTeam.getRole() == null) {
            return false;
        }

        // 권한의 퍼미션 확인
        return rolePermissionRepository.existsByRole_RoleIdAndId_Permission(
                userTeam.getRole().getRoleId(), permission);
    }

    @Override
    public boolean isTeamCreator(Long userId, Long teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new IllegalArgumentException("팀을 찾을 수 없습니다."));
        return team.getCreator().getUserId().equals(userId);
    }

    @Override
    @Transactional
    public void deleteTeamRoles(Long teamId) {
        // 팀의 모든 권한 조회
        List<Role> roles = roleRepository.findByTeam_TeamId(teamId);
        
        // 각 권한의 퍼미션 먼저 삭제 (외래 키 제약 조건 때문)
        for (Role role : roles) {
            rolePermissionRepository.deleteByRole_RoleId(role.getRoleId());
        }
        
        // 권한 삭제
        roleRepository.deleteByTeam_TeamId(teamId);
    }

    // 헬퍼 메서드: 권한에 퍼미션 추가
    private void addPermissionToRole(Role role, Permission permission) {
        RolePermissionId rolePermissionId = new RolePermissionId(role.getRoleId(), permission);
        RolePermission rolePermission = RolePermission.builder()
                .id(rolePermissionId)
                .role(role)
                .build();
        rolePermissionRepository.save(rolePermission);
    }
}
