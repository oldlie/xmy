package com.oldlie.health.medicalappointment.controller.api;

import com.oldlie.health.medicalappointment.model.db.Appointment;
import com.oldlie.health.medicalappointment.model.response.BaseResponse;
import com.oldlie.health.medicalappointment.model.response.ItemResponse;
import com.oldlie.health.medicalappointment.model.response.PageResponse;
import com.oldlie.health.medicalappointment.service.AppointmentService;
import com.oldlie.health.medicalappointment.util.Tools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author oldlie
 * @date 2020/10/4
 */
@RestController
@RequestMapping("/api/system")
@Validated
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @PostMapping("/appointment")
    public ItemResponse<Long> store(@RequestParam(value = "id", required = false, defaultValue = "-1") long id,
                                    @RequestParam(value = "did") long doctorId,
                                    @RequestParam(value = "doctor") String doctor,
                                    @RequestParam(value = "ymd") int ymd,
                                    @RequestParam(value = "date") String bookDate,
                                    @RequestParam(value = "week") String bookWeek,
                                    @RequestParam(value = "time") String timeRange,
                                    @RequestParam(value = "candidate") int candidate) {
        Appointment appointment = new Appointment();
        if (id > 0) {
            appointment.setId(id);
        }
        appointment.setYmd(ymd);
        appointment.setDoctorId(doctorId);
        appointment.setDoctor(doctor);
        appointment.setBookDate(bookDate);
        appointment.setBookWeek(bookWeek);
        appointment.setTimeRange(timeRange);
        appointment.setCandidateCount(candidate);
        appointment.setBookedCount(0);
        appointment.setLocked(0);
        appointment.setPublished(0);
        return appointmentService.store(appointment);
    }

    @DeleteMapping("/appointment")
    public BaseResponse delete(@RequestParam("id") long id) {
        return this.appointmentService.delete(id);
    }

    @GetMapping("/appointment")
    public ItemResponse<Appointment> one(@RequestParam("id") long id) {
        return this.appointmentService.one(id);
    }

    @GetMapping("/appointments")
    public PageResponse<Appointment> page(@RequestParam("page") int page,
                                          @RequestParam("size") int size,
                                          @RequestParam("orderBy") String orderBy,
                                          @RequestParam("direct") String direct) {
        return this.appointmentService.page(page, size, orderBy, direct);
    }
}
