package com.oldlie.health.medicalappointment.model.db;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@AllArgsConstructor
@Data
@Entity
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@Table(name = "t_role", uniqueConstraints = { @UniqueConstraint(columnNames = "roleTitle")})
@ToString
public class Role extends Base {
    private String roleTitle;
    private String roleDesc;
    public final static String ROLE_TITLE = "roleTitle";
    public final static String ROLE_DESC = "roleDesc";
}
