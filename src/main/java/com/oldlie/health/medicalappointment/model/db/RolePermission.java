package com.oldlie.health.medicalappointment.model.db;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Table;

@AllArgsConstructor
@Data
@Entity
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@Table(name = "t_role_permission")
@ToString
public class RolePermission extends Base {
    public final static String ROLE = "role";
    public final static String PID = "pid";
    private long rid;
    private String role;
    private long pid;
}
