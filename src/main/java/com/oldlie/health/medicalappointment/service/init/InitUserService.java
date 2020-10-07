package com.oldlie.health.medicalappointment.service.init;

import com.oldlie.health.medicalappointment.model.Csp;
import com.oldlie.health.medicalappointment.model.db.*;
import com.oldlie.health.medicalappointment.model.db.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class InitUserService {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private PermissionRepository permissionRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private RolePermissionRepository rolePermissionRepository;

    public void init() {
        long count = this.roleRepository.count(
                (root, criteriaQuery, criteriaBuilder) ->
                        criteriaBuilder.equal(root.get(Role.ROLE_TITLE), Csp.ADMIN)
        );
        Role role;
        if (count <= 0) {
            role = new Role();
            role.setRoleTitle(Csp.ADMIN);
            role.setRoleDesc("管理员");
            role = this.roleRepository.save(role);
        } else {
            Optional<Role> optional = this.roleRepository.findOne(
                    (root, criteriaQuery, criteriaBuilder) ->
                            criteriaBuilder.equal(root.get(Role.ROLE_TITLE), Csp.ADMIN)
            );
            role = optional.get();
        }

        this.initRolePermission(role);

        Optional<User> optionalUser = this.userRepository.findOne(
                (root, criteriaQuery, criteriaBuilder) ->
                        criteriaBuilder.equal(root.get(User.USERNAME), "maadmin")
        );
        if (!optionalUser.isPresent()) {
            User user = new User();
            user.setUsername("maadmin");
            user.setNickname("系统管理员");
            user.setPassword(this.bCryptPasswordEncoder.encode("qiong0214"));
            List<Role> roles = new LinkedList<>();
            roles.add(role);
            user.setRoles(roles);
            user = this.userRepository.save(user);
            Patient patient = new Patient();
            patient.setUid(user.getId());
            patient.setName("系统管理员");
            this.patientRepository.save(patient);
        }

        count = this.roleRepository.count(
                (root, criteriaQuery, criteriaBuilder) ->
                        criteriaBuilder.equal(root.get(Role.ROLE_TITLE), Csp.USER)
        );
        if (count <= 0) {
            role = new Role();
            role.setRoleTitle(Csp.USER);
            role.setRoleDesc("用户");
            role = this.roleRepository.save(role);
            try2ddPermission2role(role, "user");
        }
    }

    private void try2ddPermission2role(Role role, String permissionTitle) {
        Optional<Permission> optional = this.permissionRepository.findOne(
                (root, criteriaQuery, criteriaBuilder) ->
                        criteriaBuilder.equal(root.get(Permission.PER_KEY), permissionTitle)
        );
        if (optional.isPresent()) {
            Permission permission = optional.get();
            Optional<RolePermission> rolePermissionOptional = this.rolePermissionRepository.findOne(
                    (root, criteriaQuery, criteriaBuilder) -> {
                        Predicate predicate =
                                criteriaBuilder.equal(root.get(RolePermission.PID), permission.getId());
                        Predicate predicate1 =
                                criteriaBuilder.equal(root.get(RolePermission.ROLE), role.getRoleTitle());
                        return criteriaBuilder.and(predicate, predicate1);
                    }
            );
            if (!rolePermissionOptional.isPresent()) {
                RolePermission rolePermission = new RolePermission();
                rolePermission.setRid(role.getId());
                rolePermission.setRole(role.getRoleTitle());
                rolePermission.setPid(optional.get().getId());
                this.rolePermissionRepository.save(rolePermission);
            }
        }
    }

    private void initRolePermission(Role role) {

        String[] permissionTitles = new String[] {
                "system", "user", "doctor", "image"
        };

        for (String permissionTitle : permissionTitles) {
            this.try2ddPermission2role(role, permissionTitle);
        }
    }
}
