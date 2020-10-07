package com.oldlie.health.medicalappointment.controller;

import com.oldlie.health.medicalappointment.exception.AppRestException;
import com.oldlie.health.medicalappointment.model.response.BaseResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author oldlie
 * @date 2020/10/3
 */
@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler(AppRestException.class)
    public BaseResponse exceptionHandle(AppRestException ex) {
        BaseResponse response = new BaseResponse();
        return response.setFailed(ex.getLocalizedMessage());
    }
}
