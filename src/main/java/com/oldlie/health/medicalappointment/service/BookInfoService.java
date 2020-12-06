package com.oldlie.health.medicalappointment.service;

import com.oldlie.health.medicalappointment.model.Csp;
import com.oldlie.health.medicalappointment.model.db.*;
import com.oldlie.health.medicalappointment.model.db.repository.*;
import com.oldlie.health.medicalappointment.model.response.BaseResponse;
import com.oldlie.health.medicalappointment.model.response.ItemResponse;
import com.oldlie.health.medicalappointment.model.response.PageResponse;
import com.oldlie.health.medicalappointment.util.Tools;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * @author oldlie
 * @date 2020/10/6
 */
@Service
@Slf4j
public class BookInfoService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private BookInfoRepository bookInfoRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private UserRepository userRepository;

    public ItemResponse<Long> store(BookInfo bookInfo) {
        ItemResponse<Long> response = new ItemResponse<>();

        return response;
    }

    @Transactional(rollbackFor = Exception.class)
    public ItemResponse<BookInfo> book(long uid, long did, long aid, String nickname, String phone) {
        ItemResponse<BookInfo> response = new ItemResponse<>();

        // 防止同一个账号重复预约
        Optional<BookInfo> optional = this.bookInfoRepository.findOne((root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.equal(root.get(BookInfo.UID), uid);
            Predicate predicate1 = criteriaBuilder.equal(root.get(BookInfo.AID), aid);
            Predicate predicate2 = criteriaBuilder.equal(root.get(BookInfo.CANCELED), Csp.FALSE_INT);
            return criteriaBuilder.and(predicate, predicate1, predicate2);
        });
        if (optional.isPresent()) {
            response.setFailed("您已经预约了这个时段");
            return response;
        }

        Appointment appointment = this.appointmentRepository.findOne(
                (root, query, criteriaBuilder) -> {
                    Predicate predicate = criteriaBuilder.equal(root.get(Appointment.ID), aid);
                    Predicate predicate1 = criteriaBuilder.equal(root.get(Appointment.DOCTOR_ID), did);
                    return criteriaBuilder.and(predicate, predicate1);
                }
        ).orElse(null);
        if (appointment == null) {
            response.setFailed("预约信息不存在了");
            return response;
        }

        long ymd = appointment.getYmd();
        optional = this.bookInfoRepository.findOne(
                (root, criteriaQuery, criteriaBuilder) -> {
                    Predicate predicate = criteriaBuilder.equal(root.get(BookInfo.YMD), ymd);
                    Predicate predicate1 = criteriaBuilder.equal(root.get(BookInfo.UID), uid);
                    Predicate predicate2 = criteriaBuilder.equal(root.get(BookInfo.CANCELED), Csp.FALSE_INT);
                    return criteriaBuilder.and(predicate, predicate1, predicate2);
                }
        );
        if (optional.isPresent()) {
            response.setFailed("您今天已经有预约了，请前往预约记录查看");
            return response;
        }

        User user = this.userRepository.findById(uid).orElse(null);
        if (user == null) {
            response.setFailed("非法访问");
            return response;
        }
        Doctor doctor = this.doctorRepository.findOne(
                (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(Doctor.ID), did)
        ).orElse(null);
        if (doctor == null) {
            response.setFailed("医生信息不存在了");
            return response;
        }


        if (appointment.getBookedCount() >= appointment.getCandidateCount()) {
            response.setFailed("这个时段已经预约满了，请选择其他时段");
            return response;
        }

        Patient patient = this.patientRepository.findOne(
                (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(Patient.UID), uid)
        ).orElse(null);
        if (patient == null) {
            response.setStatus(4);
            response.setMessage("请先完善您的资料");
        }

        LocalDateTime localDateTime = LocalDateTime.now();
        BookInfo bookInfo = new BookInfo();
        bookInfo.setUid(uid);
        bookInfo.setUsername(user.getUsername());
        bookInfo.setNickname(nickname);
        bookInfo.setPhone(phone);
        bookInfo.setDid(did);
        bookInfo.setAid(aid);
        bookInfo.setDoctor(doctor.getDoctorName());
        bookInfo.setBookDate(appointment.getBookDate());
        bookInfo.setBookWeek(appointment.getBookWeek());
        bookInfo.setTimeRange(appointment.getTimeRange());
        bookInfo.setYmd(appointment.getYmd());
        bookInfo = this.bookInfoRepository.save(bookInfo);
        response.setItem(bookInfo);

        user.setNickname(nickname);
        this.userRepository.save(user);

        appointment.setLocked(Csp.TRUE_INT);;
        appointment.setBookedCount(appointment.getBookedCount() + 1);
        this.appointmentRepository.save(appointment);
        return response;
    }

    public ItemResponse<Map<String, Object>> preInfo(long uid, long aid) {
        ItemResponse<Map<String, Object>> response = new ItemResponse<>();
        Optional<User> optional = this.userRepository.findById(uid);
        if(!optional.isPresent()) {
            response.setFailed("非法访问");
            return response;
        }
        Optional<Appointment> appointmentOptional = this.appointmentRepository.findById(aid);
        if (!appointmentOptional.isPresent()) {
            response.setFailed("预约信息不存在了，请退回重试");
            return response;
        }
        User user = optional.get();
        Appointment appointment = appointmentOptional.get();
        HashMap<String, Object> map = new HashMap<>();
        map.put("nickname", user.getNickname());
        map.put("phone", user.getUsername());
        map.put("appointment", appointment);
        response.setItem(map);
        return response;
    }

    public PageResponse<BookInfo> page(long uid, int index, int size) {
        PageResponse<BookInfo> response = new PageResponse<>();
        Page<BookInfo> page = this.bookInfoRepository.findAll(
                (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(BookInfo.UID), uid),
                Tools.pageable(index, size, BookInfo.YMD, Tools.DESC)
        );
        response.setList(page.getContent());
        response.setTotal(page.getTotalElements());
        return response;
    }

    @Transactional
    public BaseResponse cancel(long uid, long id) {
        BaseResponse response = new BaseResponse();
        try {
            BookInfo bookInfo = this.bookInfoRepository.findOne(
                    (root, query, criteriaBuilder) -> {
                        Predicate predicate = criteriaBuilder.equal(root.get(BookInfo.UID), uid);
                        Predicate predicate1 = criteriaBuilder.equal(root.get(BookInfo.ID), id);
                        return criteriaBuilder.and(predicate, predicate1);
                    }
            ).get();
            bookInfo.setCanceled(Csp.TRUE_INT);
            bookInfo.setCanceledBy(uid);
            bookInfo.setCanceledReason("用户自行取消");

            Appointment appointment = this.appointmentRepository.findById(bookInfo.getAid()).orElse(null);
            if (appointment != null) {
                int count = appointment.getBookedCount();
                count = count > 1 ? count - 1 : 0;
                appointment.setBookedCount(count);
                this.appointmentRepository.save(appointment);
            }

            this.bookInfoRepository.save(bookInfo);
        } catch (NoSuchElementException e) {
            response.setFailed("找不到要取消的预约记录了");
        }

        return response;
    }

    public PageResponse<BookInfo> page(long did, long ymd, int canceled, int index, int size) {
        PageResponse<BookInfo> response = new PageResponse<>();
        if (ymd <= 0) {
            response.setFailed("请指定一天查看预约");
            return response;
        }
        Page<BookInfo> page;
        if (did > 0) {
            if (canceled == -1)  {
                // 部分状态查询当天所有的
                page = this.bookInfoRepository.findAll(
                        (root, query, criteriaBuilder) -> {
                            Predicate predicate = criteriaBuilder.equal(root.get(BookInfo.DID), did);
                            Predicate predicate1 = criteriaBuilder.equal(root.get(BookInfo.YMD), ymd);
                            return criteriaBuilder.and(predicate, predicate1);
                        },
                        Tools.pageable(index, size)
                );
            } else {
                page = this.bookInfoRepository.findAll(
                        (root, query, criteriaBuilder) -> {
                            Predicate predicate = criteriaBuilder.equal(root.get(BookInfo.DID), did);
                            Predicate predicate1 = criteriaBuilder.equal(root.get(BookInfo.YMD), ymd);
                            Predicate predicate2 = criteriaBuilder.equal(root.get(BookInfo.CANCELED), canceled);
                            return criteriaBuilder.and(predicate, predicate1, predicate2);
                        },
                        Tools.pageable(index, size)
                );
            }
        } else {
            if (canceled == -1) {
                page = this.bookInfoRepository.findAll(
                        (root, query, criteriaBuilder) -> {
                            return criteriaBuilder.equal(root.get(BookInfo.YMD), ymd);
                        },
                        Tools.pageable(index, size)
                );
            } else {
                page = this.bookInfoRepository.findAll(
                        (root, query, criteriaBuilder) -> {
                            Predicate predicate1 = criteriaBuilder.equal(root.get(BookInfo.YMD), ymd);
                            Predicate predicate2 = criteriaBuilder.equal(root.get(BookInfo.CANCELED), canceled);
                            return criteriaBuilder.and(predicate1, predicate2);
                        },
                        Tools.pageable(index, size)
                );
            }
        }
        response.setTotal(page.getTotalElements());
        response.setList(page.getContent());
        return response;
    }
}
