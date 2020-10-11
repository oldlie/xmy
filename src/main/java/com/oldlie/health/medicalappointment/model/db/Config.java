package com.oldlie.health.medicalappointment.model.db;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Table;

@AllArgsConstructor
@Data
@Entity
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@Table(name = "t_config")
@ToString
public class Config extends Base {

    public final static String CONF_KEY = "confKey";
    public final static String CONFIG_GROUP = "configGroup";

    private int configGroup;
    private String groupName;
    private String confKey;
    private String confValue;
    private String confTitle;
    private String confComment;
}
