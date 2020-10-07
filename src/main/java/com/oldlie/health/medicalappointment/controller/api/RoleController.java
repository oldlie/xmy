package com.oldlie.health.medicalappointment.controller.api;

import com.oldlie.health.medicalappointment.model.db.Role;
import com.oldlie.health.medicalappointment.model.response.BaseResponse;
import com.oldlie.health.medicalappointment.model.response.ItemResponse;
import com.oldlie.health.medicalappointment.model.response.ListResponse;
import com.oldlie.health.medicalappointment.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @GetMapping("/roles")
    public ListResponse<Role> roleList() {
        return this.roleService.roleList();
    }

    @PostMapping("/role")
    public ItemResponse<Long> store(@RequestParam("id") long id,
                                    @RequestParam("role") String role,
                                    @RequestParam("desc") String desc) {
        return this.roleService.store(id, role, desc);
    }

    @DeleteMapping("/role")
    public BaseResponse delete(@RequestParam("role") String role) {
        return this.roleService.delete(role);
    }
}
