package com.oldlie.health.medicalappointment.service.init.permission;

import com.oldlie.health.medicalappointment.model.PermissionCsp;
import com.oldlie.health.medicalappointment.model.db.Permission;

public class SystemPermission implements PermissionInit {

    private Permission permission;

    public SystemPermission() {
        permission = new Permission();
        permission.setPerKey("system");
        permission.setPerMethod("*");
        permission.setPerUrl("/api/system/**");
        permission.setPerType(PermissionCsp.TYPE_API);
        permission.setPerTitle("系统管理");
    }

    @Override
    public Permission getPermission() {
        return this.permission;
    }
}
