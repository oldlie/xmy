package com.oldlie.health.medicalappointment.filter.sms;

import com.oldlie.health.medicalappointment.model.db.User;
import com.oldlie.health.medicalappointment.service.SmsCodeService;
import com.oldlie.health.medicalappointment.service.UserService;
import lombok.Data;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Objects;

/**
 * @author oldlie
 * @date 2020/10/6
 */
@Data
public class SmsCodeAuthenticationProvider implements AuthenticationProvider {

    private SmsCodeService smsCodeService;
    private UserService userService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        SmsCodeAuthenticationToken authenticationToken = (SmsCodeAuthenticationToken) authentication;
        String phone = (String)authenticationToken.getPrincipal();
        String code = (String)authenticationToken.getCredentials();
        if (!this.smsCodeService.verify(phone, code)) {
            return null;
        }
        UserDetails userDetails = this.userService.loadUserByUsername(phone);
        if (Objects.isNull(userDetails)) {
            /// throw new InternalAuthenticationServiceException("无法获取用户信息");
            // 短信用户可以先创建账号信息
            User user = this.userService.buildUserByPhone(phone);
            userDetails = (UserDetails) user;
        }

        SmsCodeAuthenticationToken authenticationResult = new SmsCodeAuthenticationToken(phone,
                code,
                userDetails.getAuthorities());
        authenticationResult.setDetails(authenticationToken.getDetails());

        return authenticationResult;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return SmsCodeAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
