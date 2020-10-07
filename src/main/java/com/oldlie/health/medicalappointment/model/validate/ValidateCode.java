package com.oldlie.health.medicalappointment.model.validate;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author oldlie
 * @date 2020/10/6
 */
@Data
public class ValidateCode {

    private String code;
    private LocalDateTime expireTime;

    public ValidateCode(String code, long expireSeconds) {
        this.code = code;
        this.expireTime = LocalDateTime.now().plusSeconds(expireSeconds);
    }

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(getExpireTime());
    }
}
