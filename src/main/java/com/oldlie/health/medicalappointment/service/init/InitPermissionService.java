package com.oldlie.health.medicalappointment.service.init;

import com.oldlie.health.medicalappointment.model.db.Permission;
import com.oldlie.health.medicalappointment.model.db.repository.PermissionRepository;
import com.oldlie.health.medicalappointment.service.init.permission.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class InitPermissionService {

    @Autowired
    private PermissionRepository permissionRepository;

    public void init() {
        this
                .try2CreatePermission(new DoctorPermission())
                .try2CreatePermission(new SystemPermission())
                .try2CreatePermission(new UserPermission())
                .try2CreatePermission(new ImagePermission())
        ;
    }

    public InitPermissionService try2CreatePermission(PermissionInit permissionInit) {
        Permission permission = permissionInit.getPermission();
        if (permission == null) {
            return this;
        }
        Optional<Permission> optional = this.permissionRepository.findOne(
                (root, criteriaQuery, criteriaBuilder) ->
                        criteriaBuilder.equal(root.get(Permission.PER_KEY), permission.getPerKey())
        );
        if (!optional.isPresent()) {
            this.permissionRepository.save(permission);
        }
        return this;
    }
}
