package com.oldlie.health.medicalappointment.controller.api;

import com.oldlie.health.medicalappointment.model.db.Permission;
import com.oldlie.health.medicalappointment.model.db.Role;
import com.oldlie.health.medicalappointment.model.response.BaseResponse;
import com.oldlie.health.medicalappointment.model.response.ListResponse;
import com.oldlie.health.medicalappointment.service.PermissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/api")
public class PermissionController {

    @Autowired
    private PermissionService permissionService;

    @GetMapping("/permissions")
    public ListResponse<Permission> permissionList() {
        return this.permissionService.permissionList();
    }

    @GetMapping("/permissions-of-role")
    public ListResponse<Permission> rolePermissionList(@RequestParam("role") String role) {
        return this.permissionService.rolePermissionList(role);
    }

    @PutMapping("/role-permission")
    public BaseResponse configRolePermission(@RequestParam("role") String role,
                                             @RequestParam("pid") long pid) {
        return this.permissionService.configRolePermission(role, pid);
    }

    @DeleteMapping("/role-permission")
    public BaseResponse cancelRolePermission(@RequestParam("role") String role,
                                             @RequestParam("pid") long pid) {
        return this.permissionService.cancelRolePermission(role,pid);
    }
}
