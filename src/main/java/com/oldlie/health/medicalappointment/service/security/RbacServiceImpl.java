package com.oldlie.health.medicalappointment.service.security;

import com.oldlie.health.medicalappointment.model.db.Permission;
import com.oldlie.health.medicalappointment.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author chenlie
 */
@Component("rbacService")
public class RbacServiceImpl implements RbacService {

    private AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Autowired
    private PermissionService permissionService;

    @Override
    public boolean hasPermission(HttpServletRequest request, Authentication authentication) {
        Object principal = authentication.getPrincipal();
        String username = principal.toString();
        List<Permission> permissions = this.permissionService.userPermissionUrls(username);
        if (permissions == null) {
            return false;
        }
        for (Permission permission : permissions) {
            if (this.antPathMatcher.match(permission.getPerUrl(), request.getRequestURI())) {
                if ("*".equals(permission.getPerMethod()) ||
                        request.getMethod()
                                .toLowerCase()
                                .equals(
                                        permission.getPerMethod().toLowerCase()
                                )
                ) {
                    return true;
                }
            }
        }
        return false;
    }
}
