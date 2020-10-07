package com.oldlie.health.medicalappointment.model.db;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author oldlie
 * @date 2020/10/4
 */
@AllArgsConstructor
@Builder
@Data
@Entity
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@Table(name = "t_sms_code")
@ToString
public class SmsCode extends Base {
    public static final String CODE = "code";
    public static final String PHONE = "phone";
    public static final String INVALID = "invalid";
    private String code;
    private String phone;
    @Column(columnDefinition = "tinyint default 0 comment '是否有效，0有效，1表示已经使用过了，现在无效'")
    private int invalid;
}
