package com.oldlie.health.medicalappointment.model;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.oldlie.health.medicalappointment.model.db.BookInfo;
import lombok.Data;

/**
 * @author oldlie
 * @date 2020/10/23
 */
@Data
public class ExportBookInfo {
    @ExcelProperty("姓名")
    private String nickname;

    @ExcelProperty("手机号码")
    private String phone;

    @ExcelProperty("医生")
    private String doctor;

    @DateTimeFormat("yyyy年MM月dd日")
    @ExcelProperty("日期")
    private String date;

    @ExcelProperty("星期")
    private String week;

    @ExcelProperty("时段")
    private String timeRange;

    @ExcelProperty("是否取消")
    private String canceled;

    @ExcelProperty("取消原因")
    private String canceledReason;

    public static ExportBookInfo getInstance(BookInfo bookInfo) {
        ExportBookInfo info = new ExportBookInfo();
        info.setNickname(bookInfo.getNickname());
        info.setPhone(bookInfo.getPhone());
        info.setDoctor(bookInfo.getDoctor());
        info.setDate(bookInfo.getBookDate());
        info.setWeek(bookInfo.getBookWeek());
        info.setTimeRange(bookInfo.getTimeRange());
        info.setCanceled(bookInfo.getCanceled() == 1 ? "是" : "");
        info.setCanceledReason(bookInfo.getCanceledReason());
        return info;
    }
}
