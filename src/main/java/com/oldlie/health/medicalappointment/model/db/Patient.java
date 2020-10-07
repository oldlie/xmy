package com.oldlie.health.medicalappointment.model.db;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@AllArgsConstructor
@Data
@Entity
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@Table(name = "t_patient")
public class Patient extends Base {
    public static final String UID = "uid";
    private long uid;
    private String name;
    @Column(columnDefinition = "int default 0 comment '0,保密；1，男，2，女'")
    private int gender;
    private int birthday;
}
