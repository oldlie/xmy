package com.oldlie.health.medicalappointment.model.db.repository;

import com.oldlie.health.medicalappointment.model.db.SmsCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author oldlie
 * @date 2020/10/4
 */
public interface SmsCodeRepository extends JpaRepository<SmsCode, Long>, JpaSpecificationExecutor<SmsCode> {
}
