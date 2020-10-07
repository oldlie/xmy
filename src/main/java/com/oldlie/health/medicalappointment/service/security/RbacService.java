package com.oldlie.health.medicalappointment.service.security;

import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;

public interface RbacService {

    boolean hasPermission(HttpServletRequest request, Authentication authentication);
}
