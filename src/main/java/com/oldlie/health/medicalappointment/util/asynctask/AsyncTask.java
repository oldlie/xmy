package com.oldlie.health.medicalappointment.util.asynctask;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JsonIgnore
    private AsyncTaskExecutor executor;

    @JsonIgnore
    public AsyncTask updateStatus(AsyncTaskStatus status) {
        this.status = status.getStatus();
        this.executeMessage = status.getTitle();
        return this;
    }

    @JsonIgnore
    public AsyncTask finish(AsyncTaskStatus status) {
        this.status = status.getStatus();
        this.executeMessage = status.getTitle();
        this.setEndTime(LocalDateTime.now());
        return this;
    }
}
