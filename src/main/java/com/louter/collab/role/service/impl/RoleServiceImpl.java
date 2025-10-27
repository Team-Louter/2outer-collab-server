package com.louter.collab.role.service.impl;

import com.louter.collab.common.exception.IllegalArgumentException;
import com.louter.collab.role.domain.Role;
import com.louter.collab.role.domain.RolePermission;
import com.louter.collab.role.domain.RolePermissionId;
import com.louter.collab.role.repository.RolePermissionRepository;
import com.louter.collab.role.repository.RoleRepository;
import com.louter.collab.role.service.RoleService;
import com.louter.collab.team.domain.Team;
import com.louter.collab.team.domain.UserTeam;
import com.louter.collab.team.repository.TeamRepository;
import com.louter.collab.team.repository.UserTeamRepository;
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

        // 멤버 권한 생성
        Role memberRole = Role.builder()
                .team(team)
                .roleName("멤버")
                .permissions(new HashSet<>())
                .build();
        memberRole = roleRepository.save(memberRole);

        // 기본 퍼미션 추가 (READ)
        addPermissionToRole(memberRole, "READ");

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
                .permissions(new HashSet<>())
                .build();
        adminRole = roleRepository.save(adminRole);

        // 관리자 퍼미션 추가
        addPermissionToRole(adminRole, "READ");
        addPermissionToRole(adminRole, "WRITE");
        addPermissionToRole(adminRole, "DELETE");
        addPermissionToRole(adminRole, "KICK_MEMBER");
        addPermissionToRole(adminRole, "APPROVE_MEMBER");
        addPermissionToRole(adminRole, "MANAGE_ROLES");

        return adminRole;
    }

    @Override
    @Transactional
    public Role createCustomRole(Long userId, Long teamId, String roleName, Set<String> permissions) {
        // MANAGE_ROLES 권한 확인
        if (!hasPermission(userId, teamId, "MANAGE_ROLES")) {
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
                .permissions(new HashSet<>())
                .build();
        customRole = roleRepository.save(customRole);

        // 퍼미션 추가
        if (permissions != null && !permissions.isEmpty()) {
            for (String permission : permissions) {
                addPermissionToRole(customRole, permission);
            }
        }

        return customRole;
    }

    @Override
    @Transactional
    public void deleteRole(Long userId, Long teamId, Long roleId) {
        // MANAGE_ROLES 권한 확인
        if (!hasPermission(userId, teamId, "MANAGE_ROLES")) {
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
    public void addPermission(Long userId, Long teamId, Long roleId, String permission) {
        // MANAGE_ROLES 권한 확인
        if (!hasPermission(userId, teamId, "MANAGE_ROLES")) {
            throw new IllegalArgumentException("권한을 관리할 권한이 없습니다.");
        }

        // 권한 존재 확인
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new IllegalArgumentException("권한을 찾을 수 없습니다."));

        // 해당 팀의 권한인지 확인
        if (!role.getTeam().getTeamId().equals(teamId)) {
            throw new IllegalArgumentException("해당 팀의 권한이 아닙니다.");
        }

        // 이미 퍼미션이 있는지 확인
        if (rolePermissionRepository.existsByRole_RoleIdAndId_Permission(roleId, permission)) {
            throw new IllegalArgumentException("이미 존재하는 퍼미션입니다.");
        }

        // 퍼미션 추가
        addPermissionToRole(role, permission);
    }

    @Override
    @Transactional
    public void removePermission(Long userId, Long teamId, Long roleId, String permission) {
        // MANAGE_ROLES 권한 확인
        if (!hasPermission(userId, teamId, "MANAGE_ROLES")) {
            throw new IllegalArgumentException("권한을 관리할 권한이 없습니다.");
        }

        // 권한 존재 확인
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new IllegalArgumentException("권한을 찾을 수 없습니다."));

        // 해당 팀의 권한인지 확인
        if (!role.getTeam().getTeamId().equals(teamId)) {
            throw new IllegalArgumentException("해당 팀의 권한이 아닙니다.");
        }

        // 퍼미션 존재 확인
        RolePermissionId rolePermissionId = new RolePermissionId(roleId, permission);
        RolePermission rolePermission = rolePermissionRepository.findById(rolePermissionId)
                .orElseThrow(() -> new IllegalArgumentException("퍼미션을 찾을 수 없습니다."));

        // 퍼미션 삭제
        rolePermissionRepository.delete(rolePermission);
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
    public boolean hasPermission(Long userId, Long teamId, String permission) {
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

    // 헬퍼 메서드: 권한에 퍼미션 추가
    private void addPermissionToRole(Role role, String permission) {
        RolePermissionId rolePermissionId = new RolePermissionId(role.getRoleId(), permission);
        RolePermission rolePermission = RolePermission.builder()
                .id(rolePermissionId)
                .role(role)
                .build();
        rolePermissionRepository.save(rolePermission);
    }
}
