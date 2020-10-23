package com.oldlie.health.medicalappointment.controller;

import com.oldlie.health.medicalappointment.model.response.BaseResponse;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class HomeController {

    @GetMapping("/")
    public BaseResponse home() {
        BaseResponse response = new BaseResponse();
        response.setMessage("API worked!");
        log.warn("API worked!");
        return response;
    }
}
