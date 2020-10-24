package com.oldlie.health.medicalappointment.controller.api;

import com.oldlie.health.medicalappointment.model.response.ItemResponse;
import com.oldlie.health.medicalappointment.service.FileService;
import com.oldlie.health.medicalappointment.util.asynctask.AsyncTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author oldlie
 * @date 2020/10/3
 */
@RestController
@RequestMapping("/api")
public class FileController {

    @Autowired
    private FileService fileService;

    @GetMapping("/file/export/book-info")
    public ItemResponse<Map<String, Object>> exportBookInfo(
            @RequestParam(value = "did", required = false, defaultValue = "0") long did,
            @RequestParam("start") int start,
            @RequestParam("end") int end,
            @RequestParam(value = "canceled", required = false, defaultValue = "-1") int canceled,
            @SessionAttribute("username") String username) {
        return this.fileService.exportBookInfo(username, did, start, end, canceled);
    }

    @GetMapping("/file/export/status")
    public ItemResponse<AsyncTask> exportStatus(@RequestParam("id") long id) {
        return this.fileService.taskStatus(id);
    }

}
