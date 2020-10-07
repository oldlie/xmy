package com.oldlie.health.medicalappointment.controller;

import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;
import com.oldlie.health.medicalappointment.model.db.Appointment;
import com.oldlie.health.medicalappointment.model.db.Doctor;
import com.oldlie.health.medicalappointment.model.response.BaseResponse;
import com.oldlie.health.medicalappointment.model.response.ListResponse;
import com.oldlie.health.medicalappointment.model.response.PageResponse;
import com.oldlie.health.medicalappointment.service.AppointmentService;
import com.oldlie.health.medicalappointment.service.DoctorService;
import com.oldlie.health.medicalappointment.service.SmsCodeService;
import com.oldlie.health.medicalappointment.util.Tools;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;

/**
 * @author oldlie
 * @date 2020/10/4
 */
@RestController
@RequestMapping("/pub")
public class PublicController {

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private SmsCodeService smsCodeService;

    @GetMapping("/doctors")
    public ListResponse<Doctor> doctors() {
        return this.doctorService.list(Doctor.ID, Tools.DESC);
    }

    @GetMapping("/appointments")
    public PageResponse<Appointment> appointmentList(@RequestParam("did") long doctorId,
                                                     @RequestParam("page") int page,
                                                     @RequestParam("size") int size,
                                                     @RequestParam("ob") String ob,
                                                     @RequestParam("direct") String direct) {
        return this.appointmentService.page(doctorId, page, size, ob, direct);
    }

    @Autowired
    private Producer captchaProducer;
    @GetMapping("/kaptcha")
    public void getKaptchaImage(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        response.setDateHeader("Expires", 0);
        response.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        response.addHeader("Cache-Control", "post-check=0, pre-check=0");
        response.setHeader("Pragma", "no-cache");
        response.setContentType("image/jpeg");
        //生成验证码
        String capText = captchaProducer.createText();
        session.setAttribute(Constants.KAPTCHA_SESSION_KEY, capText);
        //向客户端写出
        BufferedImage bi = captchaProducer.createImage(capText);
        ServletOutputStream out = response.getOutputStream();
        ImageIO.write(bi, "jpg", out);
        try {
            out.flush();
        } finally {
            out.close();
        }
    }

    @PostMapping("/send-sms")
    public BaseResponse sendSmsCode(@RequestParam("phone") String phone,
                                    @RequestParam("code") String code,
                                    @SessionAttribute(Constants.KAPTCHA_SESSION_KEY) String verifyCodeExpected) {
        BaseResponse response = new BaseResponse();
        if (StringUtils.equalsIgnoreCase(code, verifyCodeExpected)) {
            String smsCode = this.smsCodeService.code();
            return this.smsCodeService.save(phone, smsCode);
        } else {
            return response.setFailed("验证码不正确");
        }
    }
}
