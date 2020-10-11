package com.oldlie.health.medicalappointment.controller;

import com.oldlie.health.medicalappointment.model.response.BaseResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {


    @GetMapping("/")
    public BaseResponse home() {
        BaseResponse response = new BaseResponse();
        response.setMessage("API worked!");
        return response;
    }
}
