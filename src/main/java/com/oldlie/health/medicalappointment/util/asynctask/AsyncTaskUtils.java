package com.oldlie.health.medicalappointment.util.asynctask;

import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author oldlie
 * @date 2020/10/24
 */
@Component
@Log4j2
public class AsyncTaskUtils {

    private ConcurrentHashMap<Long, AsyncTask> taskMap = new ConcurrentHashMap<>();

    public synchronized long buildId() {
        return System.currentTimeMillis();
    }

    public synchronized AsyncTask buildTask(String title, String comment, AsyncTaskExecutor executor){
        AsyncTask task = new AsyncTask();
        task.setId(this.buildId());
        task.setTitle(title);
        task.setComment(comment);
        task.setExecutor(executor);
        task.setStartTime(LocalDateTime.now());
        return task;
    }

    @Async
    public synchronized AsyncTask start(AsyncTask task) {
        int maxTasks = 2;
        int expired = 1;
        if (this.taskMap.size() > maxTasks) {
            // 删除已经执行完了且超过保留时间的任务
            for (long id : this.taskMap.keySet()) {
                AsyncTask temp = this.taskMap.get(id);
                LocalDateTime finishTime = temp.getEndTime();
                LocalDateTime expiredTime = finishTime.plusMinutes(expired);
                if (LocalDateTime.now().isAfter(expiredTime) && this.isFinished(temp)) {
                    // 当前时间在超时时间之后且状态时不在运行状态可以删除
                    this.taskMap.remove(temp.getId());
                }
            }
            if (this.taskMap.size() > maxTasks) {
                return task.finish(AsyncTaskStatus.MAX_LIMIT);
            }
        }
        if (this.taskMap.containsKey(task.getId())) {
            return task.finish(AsyncTaskStatus.EXISTED);
        }
        task.updateStatus(AsyncTaskStatus.INIT);
        this.taskMap.putIfAbsent(task.getId(), task);
        try {
            AsyncTaskExecutor executor = task.getExecutor();
            task.updateStatus(AsyncTaskStatus.PROCESSING);
            executor.execute();
            task.finish(AsyncTaskStatus.FINISH);
        } catch (Exception e) {
            log.error(e.getMessage());
            task.finish(AsyncTaskStatus.INTERRUPT);
        }
        return task;
    }

    public AsyncTask info(long id) {
        AsyncTask task = this.taskMap.get(id);
        if (this.isFinished(task)) {
            // 如果不是初始化或者正在执行中，则表示当前任务已经完成了
            this.taskMap.remove(task.getId());
        }
        return task;
    }

    private boolean isFinished(AsyncTask task) {
        return task.getStatus() != AsyncTaskStatus.INIT.getStatus() &&
                task.getStatus() != AsyncTaskStatus.PROCESSING.getStatus();
    }
}
