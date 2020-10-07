package com.oldlie.health.medicalappointment.service;

import com.oldlie.health.medicalappointment.model.db.Role;
import com.oldlie.health.medicalappointment.model.db.RolePermission;
import com.oldlie.health.medicalappointment.model.db.repository.RolePermissionRepository;
import com.oldlie.health.medicalappointment.model.db.repository.RoleRepository;
import com.oldlie.health.medicalappointment.model.db.repository.UserRepository;
import com.oldlie.health.medicalappointment.model.response.BaseResponse;
import com.oldlie.health.medicalappointment.model.response.ItemResponse;
import com.oldlie.health.medicalappointment.model.response.ListResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private RolePermissionRepository rolePermissionRepository;

    @Autowired
    private UserRepository userRepository;

    public ItemResponse<Long> store(long id, String role, String desc) {
        ItemResponse<Long> response = new ItemResponse<>();
        Role target;
        if (id > 0) {
            Optional<Role> optional = this.roleRepository.findOne(
                    (root, criteriaQuery, criteriaBuilder) ->
                            criteriaBuilder.equal(root.get(Role.ID), id)
            );
            if (!optional.isPresent()) {
                response.setFailed("角色不存在了");
                return response;
            }
            target = optional.get();
        } else {
            target = new Role();
        }
        target.setRoleTitle(role);
        target.setRoleDesc(desc);
        target = this.roleRepository.save(target);
        response.setItem(target.getId());
        return response;
    }

    public BaseResponse delete(String role) {
        BaseResponse response = new BaseResponse();
        long count = this.roleRepository.countUsers(role);
        if (count > 0) {
            return response.setFailed("请先将该角色下的用户指定其他的角色");
        }
        List<RolePermission> list = this.rolePermissionRepository.findAll(
                (root, criteriaQuery, criteriaBuilder) ->
                        criteriaBuilder.equal(root.get(RolePermission.ROLE), role)
        );
        this.rolePermissionRepository.deleteAll(list);
        this.roleRepository.findOne(
                (root, criteriaQuery, criteriaBuilder) ->
                        criteriaBuilder.equal(root.get(Role.ROLE_TITLE), root)
        ).ifPresent(x -> this.roleRepository.delete(x));
        return response;
    }


    /**
     * 获取全部的角色列表
     * @return List of Role
     */
    public ListResponse<Role> roleList() {
        ListResponse<Role> response = new ListResponse<>();
        List<Role> list = this.roleRepository.findAll();
        response.setList(list);
        return response;
    }

}
