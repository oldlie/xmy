package com.oldlie.health.medicalappointment.service;

import com.oldlie.health.medicalappointment.model.db.Permission;
import com.oldlie.health.medicalappointment.model.db.Role;
import com.oldlie.health.medicalappointment.model.db.RolePermission;
import com.oldlie.health.medicalappointment.model.db.User;
import com.oldlie.health.medicalappointment.model.db.repository.PermissionRepository;
import com.oldlie.health.medicalappointment.model.db.repository.RolePermissionRepository;
import com.oldlie.health.medicalappointment.model.db.repository.RoleRepository;
import com.oldlie.health.medicalappointment.model.db.repository.UserRepository;
import com.oldlie.health.medicalappointment.model.response.BaseResponse;
import com.oldlie.health.medicalappointment.model.response.ListResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class PermissionService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PermissionRepository permissionRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private RolePermissionRepository rolePermissionRepository;

    @Cacheable(value = "permissionCache", key = "#username")
    public List<Permission> userPermissionUrls(String username) {
        Optional<User> optional = this.userRepository.findOne(
                (root, criteriaQuery, criteriaBuilder) ->
                        criteriaBuilder.equal(root.get(User.USERNAME), username)
        );
        if (!optional.isPresent()) {
            return null;
        }
        List<Role> roles = optional.get().getRoles();
        List<RolePermission> rolePermissions = this.rolePermissionRepository.findAll(
                (root, criteriaQuery, criteriaBuilder) -> {
                    CriteriaBuilder.In<String> in = criteriaBuilder.in(root.get(RolePermission.ROLE));
                    for (Role role : roles) {
                        in.value(role.getRoleTitle());
                    }
                    return in;
                }
        );
        List<Permission> permissions = this.permissionRepository.findAll(
                (root, criteriaQuery, criteriaBuilder) -> {
                    CriteriaBuilder.In<Long> in = criteriaBuilder.in(root.get(Permission.ID));
                    for (RolePermission role : rolePermissions) {
                        in.value(role.getPid());
                    }
                    return in;
                }
        );

        return permissions;
    }

    /**
     * 管理员配置角色的权限
     * @param role 当前role
     * @param pid 需要配置的权限
     * @return BaseResponse
     */
    @CacheEvict(value = "permissionCache")
    public BaseResponse configRolePermission(String role, long pid) {
        BaseResponse response = new BaseResponse();
        Optional<Role> optional = this.roleRepository.findOne(
                (root, criteriaQuery, criteriaBuilder) ->
                        criteriaBuilder.equal(root.get(Role.ROLE_TITLE), role)
        );
        if (!optional.isPresent()) {
            return response.setFailed("当前角色不存在了");
        }
        Role role1 = optional.get();
        Optional<RolePermission> optional1 = this.rolePermissionRepository.findOne(
                (root, criteriaQuery, criteriaBuilder) -> {
                    Predicate predicate = criteriaBuilder.equal(root.get(RolePermission.ROLE), role);
                    Predicate predicate1 = criteriaBuilder.equal(root.get(RolePermission.PID), pid);
                    return criteriaBuilder.and(predicate, predicate1);
                }
        );
        if (optional1.isPresent()) {
            return response;
        }
        RolePermission rolePermission = new RolePermission();
        rolePermission.setRid(role1.getId());
        rolePermission.setRole(role1.getRoleTitle());
        rolePermission.setPid(pid);
        this.rolePermissionRepository.save(rolePermission);
        return response;
    }

    /**
     * 管理员取消角色和权限的对应关系
     * @param role 角色
     * @param pid 权限id
     * @return BaseResponse
     */
    @CacheEvict(value = "permissionCache")
    public BaseResponse cancelRolePermission(String role, long pid) {
        BaseResponse response = new BaseResponse();
        this.rolePermissionRepository.findOne(
                (root, criteriaQuery, criteriaBuilder) -> {
                    Predicate predicate = criteriaBuilder.equal(root.get(RolePermission.ROLE), role);
                    Predicate predicate1 = criteriaBuilder.equal(root.get(RolePermission.PID), pid);
                    return criteriaBuilder.and(predicate, predicate1);
                }
        ).ifPresent(rolePermission -> this.rolePermissionRepository.delete(rolePermission));
        return response;
    }

    /**
     * 获取全部的权限列表
     * @return List Of Permission
     */
    public ListResponse<Permission> permissionList() {
        ListResponse<Permission> response = new ListResponse<>();
        List<Permission> list = this.permissionRepository.findAll();
        response.setList(list);
        return response;
    }

    /**
     * 根据角色名获取该角色已经拥有的全部权限
     * @param role 角色
     * @return List of Permission
     */
    public ListResponse<Permission> rolePermissionList(String role) {
        ListResponse<Permission> response = new ListResponse<>();
        List<RolePermission> list = this.rolePermissionRepository.findAll(
                (root, criteriaQuery, criteriaBuilder) ->
                        criteriaBuilder.equal(root.get(RolePermission.ROLE), role)
        );
        List<Permission> permissions = this.permissionRepository.findAll(
                (root, criteriaQuery, criteriaBuilder) -> {
                    CriteriaBuilder.In<Long> in = criteriaBuilder.in(root.get(Permission.ID));
                    for (RolePermission rolePermission : list) {
                        in.value(rolePermission.getPid());
                    }
                    return in;
                }
        );
        response.setList(permissions);
        return response;
    }

}
