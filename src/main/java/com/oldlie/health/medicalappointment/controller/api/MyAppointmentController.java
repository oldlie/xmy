package com.oldlie.health.medicalappointment.controller.api;

import com.oldlie.health.medicalappointment.model.db.Appointment;
import com.oldlie.health.medicalappointment.model.db.BookInfo;
import com.oldlie.health.medicalappointment.model.db.Patient;
import com.oldlie.health.medicalappointment.model.response.BaseResponse;
import com.oldlie.health.medicalappointment.model.response.ItemResponse;
import com.oldlie.health.medicalappointment.model.response.PageResponse;
import com.oldlie.health.medicalappointment.service.AppointmentService;
import com.oldlie.health.medicalappointment.service.BookInfoService;
import com.oldlie.health.medicalappointment.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @author oldlie
 * @date 2020/10/6
 */
@RestController
@RequestMapping("/api/user")
public class MyAppointmentController {

    @Autowired
    private BookInfoService bookInfoService;

    @Autowired
    private PatientService patientService;

    @GetMapping("/book-info")
    public PageResponse<BookInfo> page(@RequestParam("page") int page,
                                       @RequestParam("size") int size,
                                       @SessionAttribute("uid") long uid) {
        return this.bookInfoService.page(uid, page, size);
    }

    @GetMapping("/book-pre-info")
    public ItemResponse<Map<String, Object>> bookInfo(@RequestParam("aid") long aid,
                                                      @SessionAttribute("uid") long uid) {
        return this.bookInfoService.preInfo(uid, aid);
    }

    @DeleteMapping("/book-info")
    public BaseResponse cancel(@RequestParam("id") long id,
                               @SessionAttribute("uid") long uid) {
        return this.bookInfoService.cancel(uid, id);
    }

    @PostMapping("/book")
    public ItemResponse<BookInfo> book(@RequestParam("aid") long aid,
                             @RequestParam("did") long did,
                             @RequestParam("nn") String nickname,
                             @RequestParam("ph") String phone,
                             @SessionAttribute("uid") long uid){
        return this.bookInfoService.book(uid, did, aid, nickname, phone);
    }

    @PostMapping("/patient")
    public BaseResponse patient(@RequestBody Patient patient) {
        return this.patientService.store(patient);
    }

    @GetMapping("/patient")
    public ItemResponse<Patient> patient(@SessionAttribute("uid") long uid) {
        return this.patientService.one(uid);
    }
}
