package com.oldlie.health.medicalappointment.service;

import com.oldlie.health.medicalappointment.model.db.Patient;
import com.oldlie.health.medicalappointment.model.db.repository.PatientRepository;
import com.oldlie.health.medicalappointment.model.response.BaseResponse;
import com.oldlie.health.medicalappointment.model.response.ItemResponse;
import com.oldlie.health.medicalappointment.util.ServiceUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author oldlie
 * @date 2020/10/4
 */
@Service
@Slf4j
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;

    public BaseResponse store(Patient patient) {
        ServiceUtil<Patient, PatientRepository> util = new ServiceUtil<>(new Patient(), this.patientRepository);
        return util.store(patient);
    }

    public ItemResponse<Patient> one(long uid) {
        ItemResponse<Patient> response = new ItemResponse<>();
        Patient patient = this.patientRepository.findOne(
                (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(Patient.UID), uid)
        ).orElse(null);
        if (patient == null) {
            response.setFailed("没有获取到您的信息");
            return response;
        }
        response.setItem(patient);
        return response;
    }
}
