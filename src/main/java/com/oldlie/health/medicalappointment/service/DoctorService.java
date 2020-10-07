package com.oldlie.health.medicalappointment.service;

import com.oldlie.health.medicalappointment.model.db.Doctor;
import com.oldlie.health.medicalappointment.model.db.repository.DoctorRepository;
import com.oldlie.health.medicalappointment.model.response.BaseResponse;
import com.oldlie.health.medicalappointment.model.response.ItemResponse;
import com.oldlie.health.medicalappointment.model.response.ListResponse;
import com.oldlie.health.medicalappointment.util.ServiceUtil;
import com.oldlie.health.medicalappointment.util.Tools;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author chenlie
 */
@Service
@Slf4j
public class DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;

    ServiceUtil<Doctor, DoctorRepository> serviceUtil;

    public ItemResponse<Long> store(Doctor doctor) {
        serviceUtil = new ServiceUtil<Doctor, DoctorRepository>(new Doctor(), this.doctorRepository);
        return serviceUtil.store(doctor);
    }

    public BaseResponse delete(long id) {
        serviceUtil = new ServiceUtil<Doctor, DoctorRepository>(this.doctorRepository);
        return serviceUtil.delete(id);
    }

    public ItemResponse<Doctor> one(long id) {
        serviceUtil = new ServiceUtil<Doctor, DoctorRepository>(this.doctorRepository);
        return serviceUtil.one(id);
    }

    public ListResponse<Doctor> list(String orderBy, String direct) {
        serviceUtil = new ServiceUtil<Doctor, DoctorRepository>(this.doctorRepository);
        return serviceUtil.list(Tools.sort(orderBy, direct));
    }
}
