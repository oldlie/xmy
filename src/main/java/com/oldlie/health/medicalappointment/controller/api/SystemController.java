package com.oldlie.health.medicalappointment.controller.api;

import com.oldlie.health.medicalappointment.model.db.Config;
import com.oldlie.health.medicalappointment.model.response.BaseResponse;
import com.oldlie.health.medicalappointment.model.response.ListResponse;
import com.oldlie.health.medicalappointment.service.ConfigService;
import com.oldlie.health.medicalappointment.service.UserService;
import com.oldlie.health.medicalappointment.service.init.config.ConfigGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/system")
public class SystemController {

    @Autowired
    private ConfigService configService;
    @Autowired
    private UserService userService;

    @PostMapping("/modify-password")
    public BaseResponse modifyPassword(@RequestParam("op") String oldPassword,
                                       @RequestParam("np") String newPassword,
                                       @SessionAttribute("uid") long uid) {
        return this.userService.modifyPassword(uid, newPassword, oldPassword);
    }

    @GetMapping("/system-setting")
    public ListResponse<Config> systemConfig() {
        return this.configService.list(ConfigGroup.SYSTEM);
    }

    @GetMapping("/sms-setting")
    public ListResponse<Config> smsConfig() {
        return this.configService.list(ConfigGroup.SMS);
    }

    @PostMapping("/update-setting")
    public BaseResponse updateConfig(@RequestParam("key") String key,
                                     @RequestParam("value") String value) {
        return this.configService.update(key, value);
    }
}
