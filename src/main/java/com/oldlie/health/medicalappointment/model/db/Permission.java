package com.oldlie.health.medicalappointment.model.db;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@AllArgsConstructor
@Data
@Entity
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@Table(name = "t_permission", uniqueConstraints = { @UniqueConstraint(columnNames = "perKey") })
@ToString
public class Permission extends Base {

    public static final String PER_KEY = "perKey";
    public static final String PER_TYPE = "perType";

    /**
     *  权限的唯一标志性字符串
     */
    private String perKey;
    @Column(columnDefinition = "int default 1 comment '1,api url;2,ui resource'")
    private int perType;
    private String perUrl;
    private String perMethod;
    private String perTitle;
    private String perComment;
}
