package com.oldlie.health.medicalappointment.service;

import com.oldlie.health.medicalappointment.model.Csp;
import com.oldlie.health.medicalappointment.model.SessionUser;
import com.oldlie.health.medicalappointment.model.db.Patient;
import com.oldlie.health.medicalappointment.model.db.Role;
import com.oldlie.health.medicalappointment.model.db.User;
import com.oldlie.health.medicalappointment.model.db.repository.PatientRepository;
import com.oldlie.health.medicalappointment.model.db.repository.RoleRepository;
import com.oldlie.health.medicalappointment.model.db.repository.UserRepository;
import com.oldlie.health.medicalappointment.model.response.BaseResponse;
import com.oldlie.health.medicalappointment.model.response.ItemResponse;
import com.oldlie.health.medicalappointment.model.response.PageResponse;
import com.oldlie.health.medicalappointment.util.Tools;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import java.util.*;

@Service
@Slf4j
public class UserService implements UserDetailsService {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Optional<User> optional = this.userRepository.findOne(
                (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get(User.USERNAME), s)
        );
        User user = optional.orElse(null);
        if (user != null) {
            user.getRoles().forEach(x -> System.out.println(x.getRoleTitle()));
        }
        return user;
    }

    public void writeUserIntoSession(SessionUser user,
                                     HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.setAttribute("uid", user.getUid());
        session.setAttribute("username", user.getUsername());
        session.setAttribute("nickname", user.getNickname());
        session.setAttribute("roles", user.getRoles());

    }

    public String roles2String(User user) {
        String roles = "";
        for (Role role : user.getRoles()) {
            roles += role.getRoleTitle() + ",";
        }
        return roles;
    }

    public ItemResponse<Long> store(long id, String username, String nickname, String password, String roleStr) {
        ItemResponse<Long> response = new ItemResponse<>();

        Optional<Role> optionalRole = this.roleRepository.findOne(
                (root, criteriaQuery, criteriaBuilder) ->
                        criteriaBuilder.equal(root.get(Role.ROLE_TITLE), roleStr)
        );
        if (!optionalRole.isPresent()) {
            response.setFailed("指定的权限不存在");
            return response;
        }

        Role role = optionalRole.get();
        User target;

        if (id > 0) {
            Optional<User> optional = this.userRepository.findOne(
                    (root, criteriaQuery, criteriaBuilder) ->
                            criteriaBuilder.equal(root.get(User.ID), id)
            );
            if (!optional.isPresent()) {
                response.setFailed("用户不存在了");
                return response;
            }
            target = optional.get();
        } else {
            target = new User();
        }
        target.setUsername(username);
        target.setNickname(nickname);
        target.setPassword(this.bCryptPasswordEncoder.encode(password));
        target.setRoles(Collections.singletonList(role));
        target = this.userRepository.save(target);
        response.setItem(target.getId());
        return response;
    }

    public BaseResponse disable(long id) {
        BaseResponse response = new BaseResponse();
        this.userRepository.findOne(
                (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get(User.ID), id)
        ).ifPresent(x -> {
            x.setEnabled(false);
            this.userRepository.save(x);
        });
        return response;
    }

    public User one(String username) {
        if (StringUtils.isEmpty(username)) {
            return null;
        }
        User user = this.userRepository.findOne(
                (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get(User.USERNAME), username)
        ).orElse(null);
        if (user != null) {
            user.getRoles().forEach(x -> { x.getRoleTitle();});
        }
        return user;
    }

    public User buildUserByPhone(String phone) {

        User user = this.userRepository.findOne(
                (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(User.USERNAME), phone)
                ).orElse(null);
        if (user != null) {
            return user;
        }
        user = new User();
        user.setUsername(phone);
        user.setNickname("");
        user.setPassword(phone);

        Role role = this.roleRepository.findOne(
                (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(Role.ROLE_TITLE), Csp.USER)
        ).orElse(null);
        if (role == null) {
            throw new RuntimeException("miss User Role");
        }
        user.setRoles(Collections.singletonList(role));
        user = this.userRepository.save(user);

        Patient patient = new Patient();
        patient.setUid(user.getId());
        this.patientRepository.save(patient);

        return user;
    }

    public void save(User user) {
        this.userRepository.save(user);
    }

    public ItemResponse<User> user(String username) {
        ItemResponse<User> response = new ItemResponse<>();
        response.setItem(one(username));
        return response;
    }

    public PageResponse<User> users(String key, String value, int index, int size, String orderBy, String direct) {
        PageResponse<User> response = new PageResponse<>();
        Page<User> page;
        if (StringUtils.isEmpty(key)) {
            page =this.userRepository.findAll(
                    Tools.pageable(index, size, orderBy, direct)
            );
        } else {
            page =this.userRepository.findAll(
                    (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.like(root.get(key), "%" + value + "%"),
                    Tools.pageable(index, size, orderBy, direct)
            );
        }
        response.setTotal(page.getTotalElements());
        response.setList(page.toList());
        return response;
    }
}
