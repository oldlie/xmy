package com.oldlie.health.medicalappointment.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.oldlie.health.medicalappointment.filter.sms.SmsCodeAuthenticationProvider;
import com.oldlie.health.medicalappointment.filter.sms.SmsCodeAuthenticationToken;
import com.oldlie.health.medicalappointment.model.Csp;
import com.oldlie.health.medicalappointment.model.SessionUser;
import com.oldlie.health.medicalappointment.model.db.User;
import com.oldlie.health.medicalappointment.model.response.BaseResponse;
import com.oldlie.health.medicalappointment.model.response.ItemResponse;
import com.oldlie.health.medicalappointment.service.ConfigService;
import com.oldlie.health.medicalappointment.service.SmsCodeService;
import com.oldlie.health.medicalappointment.service.UserService;
import com.oldlie.health.medicalappointment.service.init.config.LockedTime1;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;

@Slf4j
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;
    private ConfigService configService;
    private ObjectMapper objectMapper = new ObjectMapper();
    private UserService userService;
    private SmsCodeService smsCodeService;

    public LoginFilter(AuthenticationManager authenticationManager,
                       ConfigService configService,
                       SmsCodeService smsCodeService,
                       UserService userService) {
        this.authenticationManager = authenticationManager;
        this.configService = configService;
        this.smsCodeService = smsCodeService;
        this.userService = userService;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response)
            throws AuthenticationException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        HttpSession session = request.getSession();
        session.setAttribute("username", username);

        User checkedUser = this.userService.one(username);
        if (checkedUser != null && checkedUser.getFailedCount() != 0) {
            int locked5Min = Integer.parseInt(this.configService.getValue(LockedTime1.CONF_KEY));
            long latest = checkedUser.getLastFailedTime().getTime();
            long now = System.currentTimeMillis();
            long fiveMin = 5 * 60 * 1000;
            if (checkedUser.getFailedCount() >= locked5Min && now - latest <= fiveMin) {
                return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                        username,
                        "",
                        new LinkedList<>()
                ));
            } else {
                checkedUser.setFailedCount(0);
                this.userService.save(checkedUser);
            }
        }

        if (isSmsLogin(request)) {
            // 手机短信登录
            SmsCodeAuthenticationToken authenticationToken =
                    new SmsCodeAuthenticationToken(username, password, new LinkedList<>());
            SmsCodeAuthenticationProvider provider = new SmsCodeAuthenticationProvider();
            provider.setSmsCodeService(smsCodeService);
            provider.setUserService(userService);
            List<AuthenticationProvider> list = new LinkedList<>();
            list.add(provider);
            ProviderManager providerManager = new ProviderManager(list);
            return providerManager.authenticate(authenticationToken);
        } else {
            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    username,
                    password,
                    new LinkedList<>()
            ));
        }
    }

    private boolean isSmsLogin(HttpServletRequest request) {
        String type = request.getParameter("loginType");
        return StringUtils.isNotEmpty(type) && StringUtils.equalsIgnoreCase("phone", type);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        User user;
        if (authResult instanceof SmsCodeAuthenticationToken) {
            user = this.userService.buildUserByPhone((String) authResult.getPrincipal());
        } else if (authResult instanceof UsernamePasswordAuthenticationToken) {
            user = (User) authResult.getPrincipal();
            user = this.userService.one(user.getUsername());
            user.setFailedCount(0);
            this.userService.save(user);
        } else {
            throw new RuntimeException("Unknown authentication token.");
        }

        SessionUser sessionUser = new SessionUser();
        sessionUser.setUid(user.getId());
        sessionUser.setUsername(user.getUsername());
        sessionUser.setNickname(user.getNickname());
        sessionUser.setRoles(this.userService.roles2String(user));

        this.userService.writeUserIntoSession(sessionUser, request);

        Gson gson = new Gson();
        String subject = gson.toJson(sessionUser);

        String token = Jwts.builder().setSubject(subject)
                .setExpiration(new Date(System.currentTimeMillis() + Csp.ONE_DAY))
                .signWith(SignatureAlgorithm.HS256, Csp.APP_PASSWORD)
                .compact();
        response.addHeader(Csp.AUTHORIZATION_, Csp.APP_KEY + token);

        ItemResponse<String> success = new ItemResponse<>();
        success.setItem(token);
        response.getWriter().print(objectMapper.writeValueAsString(success));
        response.getWriter().flush();
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {

        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("username");
        if (!StringUtils.isEmpty(username)) {
            User user = this.userService.one(username);
            if (user != null) {
                user.setFailedCount(user.getFailedCount() + 1);
                user.setLastFailedTime(new Date());
                this.userService.save(user);
            }
        }
        BaseResponse success = new BaseResponse();
        success.setFailed("账号或者密码错误，请重试");
        response.getWriter().print(objectMapper.writeValueAsString(success));
        response.getWriter().flush();
    }
}
