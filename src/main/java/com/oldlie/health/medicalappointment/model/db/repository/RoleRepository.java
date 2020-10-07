package com.oldlie.health.medicalappointment.model.db.repository;

import com.oldlie.health.medicalappointment.model.db.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

public interface RoleRepository extends JpaRepository<Role, Long>, JpaSpecificationExecutor<Role> {

    @Query(nativeQuery = true,
            value = "select count(t_user_roles.user_id) from t_user_roles " +
                    "join t_role on t_user_roles.roles_id = t_role.id " +
                    "where t_role.role_title=:role")
    long countUsers(String role);
}
