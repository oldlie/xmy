package com.oldlie.health.medicalappointment.exception;

import lombok.Getter;

/**
 * @author oldlie
 * @date 2020/10/3
 */
public class AppRestException extends Exception {

    @Getter
    private int code;

    @Override
    public String getLocalizedMessage() {
        return super.getLocalizedMessage();
    }

    public AppRestException(String message) {
        super(message);
    }

    public AppRestException(String message, int code) {
        super(message);
        this.code = code;
    }
}
