package com.oldlie.health.medicalappointment.controller.api;

import com.oldlie.health.medicalappointment.model.db.Doctor;
import com.oldlie.health.medicalappointment.model.response.BaseResponse;
import com.oldlie.health.medicalappointment.model.response.ItemResponse;
import com.oldlie.health.medicalappointment.model.response.ListResponse;
import com.oldlie.health.medicalappointment.service.DoctorService;
import com.oldlie.health.medicalappointment.util.Tools;
import org.joda.money.Money;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author chenlie
 */
@RestController
@RequestMapping("/api/system/")
@Validated
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    @PostMapping("/doctor")
    public ItemResponse<Long> store(@RequestParam(value = "id", required = false, defaultValue = "0") long id,
                                    @NotEmpty(message = "不能为空")
                                    @Size(max = 255, message = "最多支持255字符")
                                    @RequestParam(value = "name") String doctorName,
                                    @RequestParam(value = "headPic",
                                            required = false,
                                            defaultValue = "") String doctorHeadPic,
                                    @NotEmpty(message = "不能为空")
                                    @Size(max = 255, message = "最多支持255字符")
                                    @RequestParam(value = "title") String doctorTitle,
                                    @Size(max = 255, message = "最多支持255字符")
                                    @RequestParam(value = "desc") String desc,
                                    @RequestParam(value = "copay") String copay) {
        Doctor doctor = new Doctor();
        doctor.setId(id);
        doctor.setDoctorHeadPic(doctorHeadPic);
        doctor.setDoctorName(doctorName);
        doctor.setDoctorTitle(doctorTitle);
        doctor.setDoctorDesc(desc);
        doctor.setCopay(Money.parse(copay));
        return doctorService.store(doctor);
    }

    @DeleteMapping("/doctor")
    public BaseResponse delete(@RequestParam(value = "id") long id) {
        return this.doctorService.delete(id);
    }

    @GetMapping("/doctors")
    public ListResponse<Doctor> list() {
        return this.doctorService.list(Doctor.ID, Tools.DESC);
    }

    @GetMapping("/doctor")
    public ItemResponse<Doctor> one(@RequestParam("id") long id) {
        return this.doctorService.one(id);
    }
}
