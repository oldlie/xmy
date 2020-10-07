package com.oldlie.health.medicalappointment.model.db;

import lombok.*;
import org.hibernate.annotations.Type;
import org.joda.money.Money;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author chenlie
 */
@AllArgsConstructor
@Data
@Entity
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@Table(name = "t_doctor")
@ToString
public class Doctor extends Base {
    private String doctorHeadPic;
    private String doctorName;
    private String doctorTitle;
    private String doctorDesc;
    /**
     * 门诊费
     */
    @Type(type = "org.jadira.usertype.moneyandcurrency.joda.PersistentMoneyMinorAmount",
            parameters = {@org.hibernate.annotations.Parameter(name = "currencyCode", value = "CNY")})
    private Money Copay;
}
