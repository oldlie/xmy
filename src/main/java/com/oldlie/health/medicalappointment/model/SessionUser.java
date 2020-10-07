package com.oldlie.health.medicalappointment.model;

import lombok.Data;

/**
 * @author oldlie
 * @date 2020/10/5
 */
@Data
public class SessionUser {
    private long uid;
    private String username;
    private String nickname;
    private String roles;
}
