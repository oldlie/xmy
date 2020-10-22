package com.oldlie.health.medicalappointment.service;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.google.gson.Gson;
import com.oldlie.health.medicalappointment.model.Csp;
import com.oldlie.health.medicalappointment.model.db.SmsCode;
import com.oldlie.health.medicalappointment.model.db.repository.SmsCodeRepository;
import com.oldlie.health.medicalappointment.model.response.AliSmsResponse;
import com.oldlie.health.medicalappointment.model.response.BaseResponse;
import com.oldlie.health.medicalappointment.model.response.ItemResponse;
import com.oldlie.health.medicalappointment.service.init.config.AkiConifg;
import com.oldlie.health.medicalappointment.service.init.config.AksConfig;
import com.oldlie.health.medicalappointment.service.init.config.SmsSignName;
import com.oldlie.health.medicalappointment.service.init.config.SmsTemplateCodeConfig;
import com.oldlie.health.medicalappointment.util.Tools;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import java.util.Random;

/**
 * @author oldlie
 * @date 2020/10/4
 */
@Service
@Slf4j
public class SmsCodeService {

    @Autowired
    private ConfigService configService;
    @Autowired
    private SmsCodeRepository smsCodeRepository;

    public String code() {
        Random random = new Random(System.currentTimeMillis());
        StringBuilder builder = new StringBuilder(8);
        builder.append(random.nextInt(10))
                .append(random.nextInt(10))
                .append(random.nextInt(10))
                .append(random.nextInt(10));
        return builder.toString();
    }

    public ItemResponse<String> save(String phone, String code) {
        ItemResponse<String> response = new ItemResponse<>();
        Page<SmsCode> page = this.smsCodeRepository.findAll(
                (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.equal(root.get(SmsCode.PHONE), phone),
                Tools.pageable(1, 1, "createDate", "desc")
        );
        if (page.getTotalElements() > 0) {
            // 同一个手机号保存两次验证码中间需要间隔5分钟
            SmsCode smsCode = page.toList().get(0);
            long fiveMinutes = 5 * 60 * 1000;
            long codeTime = smsCode.getCreateDate().getTime();
            long now = System.currentTimeMillis();
            if (now - codeTime < fiveMinutes) {
                response.setFailed("请五分钟之后再试");
                return response;
            }
        }

        String accessKeyId = this.configService.getValue(AkiConifg.CONF_KEY);
        String accessSecret = this.configService.getValue(AksConfig.CONF_KEY);
        String signName = this.configService.getValue(SmsSignName.CONF_KEY);
        String templateCode = this.configService.getValue(SmsTemplateCodeConfig.CONF_KEY);

        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessSecret);
        IAcsClient client = new DefaultAcsClient(profile);
        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain("dysmsapi.aliyuncs.com");
        request.setSysVersion("2017-05-25");
        request.setSysAction("SendSms");
        request.putQueryParameter("RegionId", "cn-hangzhou");
        request.putQueryParameter("PhoneNumbers", phone);
        request.putQueryParameter("SignName", signName);
        request.putQueryParameter("TemplateCode", templateCode);
        request.putQueryParameter("TemplateParam", "{\"code\":" + code + "}");

        /*
        try {
            CommonResponse commonResponse = client.getCommonResponse(request);
            if (commonResponse.getHttpStatus() != HttpStatus.OK.value()) {
                response.setFailed("Ali sms status:" + commonResponse.getHttpStatus());
                return response;
            }
            Gson gson = new Gson();
            AliSmsResponse aliSmsResponse = gson.fromJson(commonResponse.getData(), AliSmsResponse.class);
            if (!HttpStatus.OK.name().equals(aliSmsResponse.getCode())) {
                response.setFailed("Alis sms failed:" + aliSmsResponse.getCode());
                return response;
            }
            System.out.println(commonResponse.getData());

        } catch (ServerException e) {
            e.printStackTrace();
            log.error(e.getMessage());
            response.setFailed("短信未能发送");
            return response;
        } catch (ClientException e) {
            e.printStackTrace();
            log.error(e.getMessage());
            response.setFailed("短信未能发送");
            return response;
        }
        */

        SmsCode target = new SmsCode();
        target.setPhone(phone);
        target.setCode(code);
        target = this.smsCodeRepository.save(target);
        response.setItem(code);
        return response;
    }

    public boolean verify(String phone, String code) {
        Page<SmsCode> page = this.smsCodeRepository.findAll(
                (root, query, criteriaBuilder) -> {
                    Predicate predicate = criteriaBuilder.equal(root.get(SmsCode.CODE), code);
                    Predicate predicate1 = criteriaBuilder.equal(root.get(SmsCode.PHONE), phone);
                    Predicate predicate2 = criteriaBuilder.equal(root.get(SmsCode.INVALID), Csp.FALSE_INT);
                    return criteriaBuilder.and(predicate, predicate1, predicate2);
                },
                Tools.pageable(1, 1, "createDate", "desc")
        );
        if (page.getTotalElements() <= 0) {
            return false;
        }
        SmsCode smsCode = page.getContent().get(0);
        long create = smsCode.getCreateDate().getTime();
        long now = System.currentTimeMillis();
        long fiveMin = 5 * 60 * 1000;
        if (now - create > fiveMin) {
            return false;
        }
        return true;
    }

}
