package com.oldlie.health.medicalappointment.model.db;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author oldlie
 * @date 2020/10/6
 */
@AllArgsConstructor
@Data
@Entity
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@Table(name = "t_book_info")
@ToString
public class BookInfo extends Base{

    public static final String AID = "aid";
    public static final String CANCELED = "canceled";
    public static final String DID = "did";
    public static final String UID = "uid";
    public static final String YMD = "ymd";

    private long uid;
    private String username;
    private String nickname;
    private String phone;
    private long did;
    private long aid;
    private String doctor;
    private String bookDate;
    private String bookWeek;
    private String timeRange;
    private long ymd;
    @Column(columnDefinition = "tinyint default 0 comment '1表示这个预约被取消'")
    private int canceled;
    private long canceledBy;
    private String canceledReason;
    @Column(name = "is_read", columnDefinition = "tinyint default 0 comment '1 表示这条记录已经被管理员加载过了'")
    private int read;
    @Column(columnDefinition = "tinyint default 0 comment '1 表示这条记录被管理员电话回访过了' ")
    private int callback;
    @Column(columnDefinition = "varchar(255) comment '回访做的备注'")
    private String note;
}
