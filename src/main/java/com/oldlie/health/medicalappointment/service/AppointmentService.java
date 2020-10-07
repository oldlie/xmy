package com.oldlie.health.medicalappointment.service;

import com.oldlie.health.medicalappointment.model.db.Appointment;
import com.oldlie.health.medicalappointment.model.db.repository.AppointmentRepository;
import com.oldlie.health.medicalappointment.model.response.BaseResponse;
import com.oldlie.health.medicalappointment.model.response.ItemResponse;
import com.oldlie.health.medicalappointment.model.response.PageResponse;
import com.oldlie.health.medicalappointment.util.ObjectCopy;
import com.oldlie.health.medicalappointment.util.ServiceUtil;
import com.oldlie.health.medicalappointment.util.Tools;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * @author oldlie
 * @date 2020/10/4
 */
@Service
@Slf4j
public class AppointmentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    public ItemResponse<Long> store(Appointment appointment) {
        ItemResponse<Long> response = new ItemResponse<>();
        Appointment target = null;
        if (appointment.getId() != null && appointment.getId() > 0) {
            target = this.appointmentRepository.findById(appointment.getId()).orElse(null);
            if (target == null) {
                response.setFailed("要修改的预约不存在了，请刷新重试");
                return response;
            }

            if (target.getLocked() == 1) {
                response.setFailed("已经有病人预约了，不再允许修改预约信息");
                return response;
            }
        } else {
            target = new Appointment();
        }
        ObjectCopy<Appointment> copy = new ObjectCopy<>();
        target = copy.copyValue2Entity(appointment, target);
        target = this.appointmentRepository.save(target);
        response.setItem(target.getId());
        return response;
    }

    public ItemResponse<Appointment> one(long id) {
        return new ServiceUtil<Appointment, AppointmentRepository>(this.appointmentRepository).one(id);
    }

    public BaseResponse delete(long id) {
        BaseResponse response = new BaseResponse();
        this.appointmentRepository.findById(id).ifPresent(x -> {
            if (x.getLocked() == 1) {
                response.setFailed("已经有病人在预约了，不能删除");
                return;
            }
            this.appointmentRepository.delete(x);
        });
        return new BaseResponse();
    }

    public PageResponse<Appointment> page(int page, int size, String orderBy, String direct) {
        return new ServiceUtil<Appointment, AppointmentRepository>(appointmentRepository)
                .page(Tools.pageable(page, size, orderBy,direct));
    }

    public PageResponse<Appointment> page(long doctorId, int index, int size, String ob, String direct) {
        PageResponse<Appointment> response = new PageResponse<>();
        // 仅获取大于今天的预约
        Page<Appointment> page = this.appointmentRepository.findAll(
                (root, query, criteriaBuilder) ->  {
                    Predicate predicate = criteriaBuilder.equal(root.get(Appointment.DOCTOR_ID), doctorId);
                    Predicate predicate1 = criteriaBuilder.ge(root.get(Appointment.YMD), Tools.getYmd());
                    return criteriaBuilder.and(predicate, predicate1);
                },
                Tools.pageable(index, size, ob, direct)
        );
        response.setTotal(page.getTotalElements());
        response.setList(page.getContent());
        return response;
    }
}
