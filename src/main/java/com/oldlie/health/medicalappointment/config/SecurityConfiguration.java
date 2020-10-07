package com.oldlie.health.medicalappointment.config;

import com.oldlie.health.medicalappointment.filter.AuthenticationFilter;
import com.oldlie.health.medicalappointment.filter.LoginFilter;
import com.oldlie.health.medicalappointment.filter.sms.SmsCodeAuthenticationProvider;
import com.oldlie.health.medicalappointment.service.ConfigService;
import com.oldlie.health.medicalappointment.service.SmsCodeService;
import com.oldlie.health.medicalappointment.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private ConfigService configService;
    @Autowired
    private SmsCodeService smsCodeService;
    @Autowired
    private UserService userService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        LoginFilter loginFilter = new LoginFilter(authenticationManager(), configService, smsCodeService, userService);

        http
                .csrf()
                .disable()
                .authorizeRequests()
                .antMatchers("/", "/login", "/pub/**")
                .permitAll()
                .and()
                .authorizeRequests()
                .anyRequest()
                .access("@rbacService.hasPermission(request,authentication)")
                .and()
                .addFilter(loginFilter)
                .addFilter(new AuthenticationFilter(authenticationManager(), this.userService))
        ;
    }

}
