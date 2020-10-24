package com.oldlie.health.medicalappointment.util.asynctask;

import lombok.Getter;

/**
 * @author oldlie
 * @date 2020/10/24
 */
public enum AsyncTaskStatus {

    /**
     * 0, 初始化；1，处理中；2，处理完成；3，处理中断
     */
    INIT(0, "初始化"),
    PROCESSING(1, "处理中"),
    FINISH(2, "处理完毕"),
    INTERRUPT(3, "执行中断"),
    MAX_LIMIT(4, "任务数达到上限"),
    EXISTED(5, "这个任务已经创建了");

    @Getter
    private int status;
    @Getter
    private String title;

    AsyncTaskStatus(int status, String title) {
        this.status = status;
        this.title = title;
    }
}
