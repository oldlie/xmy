package com.oldlie.health.medicalappointment.service.init.permission;

import com.oldlie.health.medicalappointment.model.PermissionCsp;
import com.oldlie.health.medicalappointment.model.db.Permission;

/**
 * @author oldlie
 * @date 2020/10/3
 */
public class ImagePermission implements PermissionInit {

    private Permission permission = new Permission();

    public ImagePermission() {
        this.permission.setPerKey("image");
        this.permission.setPerTitle("图片资源管理");
        this.permission.setPerType(PermissionCsp.TYPE_API);
        this.permission.setPerMethod("*");
        this.permission.setPerUrl("/api/image/**");
    }

    @Override
    public Permission getPermission() {
        return permission;
    }
}
