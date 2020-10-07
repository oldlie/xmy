package com.oldlie.health.medicalappointment.controller.api;

import com.oldlie.health.medicalappointment.model.db.BookInfo;
import com.oldlie.health.medicalappointment.model.response.PageResponse;
import com.oldlie.health.medicalappointment.service.BookInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author oldlie
 * @date 2020/10/7
 */
@RestController
@RequestMapping("/api/system")
public class BookInfoController {

    @Autowired
    private BookInfoService bookInfoService;

    @GetMapping("/book-info-list")
    public PageResponse<BookInfo> page(@RequestParam(name = "did", required = false, defaultValue = "0") long did,
                                       @RequestParam(name = "ymd") long ymd,
                                       @RequestParam(name = "can", required = false, defaultValue = "-1") int canceled,
                                       @RequestParam("page") int page,
                                       @RequestParam("size") int size) {
        return this.bookInfoService.page(did, ymd, canceled, page, size);
    }
}
