package com.oldlie.health.medicalappointment.controller.api;

import com.oldlie.health.medicalappointment.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author oldlie
 * @date 2020/10/3
 */
@RestController
@RequestMapping("/api")
public class FileController {

    @Autowired
    private FileService fileService;


}
