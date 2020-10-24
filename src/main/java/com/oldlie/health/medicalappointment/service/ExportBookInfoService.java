package com.oldlie.health.medicalappointment.service;

import com.oldlie.health.medicalappointment.model.response.ItemResponse;
import com.oldlie.health.medicalappointment.util.asynctask.AsyncTask;
import com.oldlie.health.medicalappointment.util.asynctask.AsyncTaskUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author oldlie
 * @date 2020/10/23
 */
@Log4j2
@Service
public class ExportBookInfoService {

    @Autowired
    private AsyncTaskUtils asyncTaskUtils;

    public ItemResponse<AsyncTask> exportBookInfo(int startYmd, int endYmd, int did, int status) {
        ItemResponse<AsyncTask> response = new ItemResponse<>();
        return response;
    }
}
