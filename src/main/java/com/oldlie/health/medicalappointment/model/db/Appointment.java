package com.oldlie.health.medicalappointment.model.db;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * 预约记录
 * @author oldlie
 */
@AllArgsConstructor
@Data
@Entity
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@Table(name = "t_appointment")
@ToString
public class Appointment extends Base {

    public final static String DOCTOR_ID = "doctorId";
    public final static String YMD = "ymd";
    public final static String PUBLISHED = "published";

    private long ymd;
    private String bookDate;
    private String bookWeek;
    private String timeRange;
    private long doctorId;
    private String doctor;
    private int candidateCount;
    private int bookedCount;
    @Column(columnDefinition = "tinyint default 0 comment '默认为0；1 表示这个预约已经有病人预约了则不再允许修改，'")
    private int locked;
    @Column(columnDefinition = "tinyint default 0 comment '默认为0表示这个预约还没有发布，即病人还看不见，1，的时候病人就可以预约了'")
    private int published;
}
