package com.oldlie.health.medicalappointment.model;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
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

    @ExcelProperty("时段")
    private String timeRange;

    @ExcelProperty("已取消")
    private String canceled;

    @ExcelProperty("取消原因")
    private String canceledReason;

}
