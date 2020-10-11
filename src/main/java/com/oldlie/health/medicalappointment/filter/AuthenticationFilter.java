package com.oldlie.health.medicalappointment.filter;

import com.google.gson.Gson;
import com.oldlie.health.medicalappointment.model.Csp;
import com.oldlie.health.medicalappointment.model.SessionUser;
import com.oldlie.health.medicalappointment.model.StatusCode;
import com.oldlie.health.medicalappointment.model.db.User;
import com.oldlie.health.medicalappointment.model.response.BaseResponse;
import com.oldlie.health.medicalappointment.service.UserService;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.util.AntPathMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @author oldlie
 * @date 2020/9/26
 */
@Slf4j
public class AuthenticationFilter extends BasicAuthenticationFilter {

    private UserService userService;

    public AuthenticationFilter(AuthenticationManager authenticationManager, UserService userService) {
        super(authenticationManager);
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        UsernamePasswordAuthenticationToken authentication;

        String header = request.getHeader(Csp.AUTHORIZATION_);

        if (StringUtils.isEmpty(header) || !header.startsWith(Csp.APP_KEY)) {
            chain.doFilter(request, response);
            return;
        }

        AntPathMatcher matcher = new AntPathMatcher();
        if (matcher.match("**/login", request.getRequestURI())) {
            chain.doFilter(request, response);
            return;
        }

        SessionUser sessionUser = null;
        try {
            sessionUser = this.readSubject(request);
            authentication = this.getAuthentication(sessionUser.getUsername());
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            chain.doFilter(request, response);
            return;
        }

        if (authentication != null && sessionUser != null) {
            this.userService.writeUserIntoSession(sessionUser, request);
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        super.doFilterInternal(request, response, chain);
    }

    private SessionUser readSubject(HttpServletRequest request) throws Exception {
        String token = request.getHeader(Csp.AUTHORIZATION_);
        if (StringUtils.isEmpty(token)) {
            return null;
        }
        String subject = Jwts.parser().setSigningKey(Csp.APP_PASSWORD)
                .parseClaimsJws(token.replace(Csp.APP_KEY, ""))
                .getBody()
                .getSubject();
        if (StringUtils.isEmpty(subject)) {
            return null;
        }
        Gson gson = new Gson();
        return gson.fromJson(subject, SessionUser.class);

    }

    private UsernamePasswordAuthenticationToken getAuthentication(String username){
        if (StringUtils.isEmpty(username)) {
            return null;
        }
        User user = this.userService.one(username);
        if (user == null) {
            return null;
        }
        return new UsernamePasswordAuthenticationToken(user.getUsername(),
                null,
                user.getAuthorities());
    }
}
