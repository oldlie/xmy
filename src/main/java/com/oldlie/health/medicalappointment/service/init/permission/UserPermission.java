package com.oldlie.health.medicalappointment.service.init.permission;

import com.oldlie.health.medicalappointment.model.PermissionCsp;
import com.oldlie.health.medicalappointment.model.db.Permission;

public class UserPermission implements PermissionInit {

    Permission permission = new Permission();

    public UserPermission() {
        permission.setPerType(PermissionCsp.TYPE_API);
        permission.setPerKey("user");
        permission.setPerTitle("用户权限");
        permission.setPerUrl("/api/user/**");
        permission.setPerMethod("*");
    }

    @Override
    public Permission getPermission() {
        return this.permission;
    }
}
