package com.oldlie.health.medicalappointment.service.init.permission;

import com.oldlie.health.medicalappointment.model.PermissionCsp;
import com.oldlie.health.medicalappointment.model.db.Permission;

public class DoctorPermission implements PermissionInit {

    private Permission permission = new Permission();

    public DoctorPermission() {
        this.permission.setPerKey("doctor");
        this.permission.setPerTitle("管理员管理医生信息");
        this.permission.setPerType(PermissionCsp.TYPE_API);
        this.permission.setPerMethod("*");
        this.permission.setPerUrl("/api/doctor/**");
    }

    @Override
    public Permission getPermission() {
        return permission;
    }
}
