package com.oldlie.health.medicalappointment.util.asynctask;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author oldlie
 * @date 2020/10/24
 */
@Data
public class AsyncTask {
    private long id;
    private String title;
    private String comment;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private int status;
    private int stepIndex;
    private int stepTotal;
    private String executeMessage;
    private AsyncTaskExecutor executor;

    public AsyncTask updateStatus(AsyncTaskStatus status) {
        this.status = status.getStatus();
        this.executeMessage = status.getTitle();
        return this;
    }

    public AsyncTask finish(AsyncTaskStatus status) {
        this.status = status.getStatus();
        this.executeMessage = status.getTitle();
        this.setEndTime(LocalDateTime.now());
        return this;
    }
}
