package com.oldlie.health.medicalappointment.model.response;

import com.oldlie.health.medicalappointment.model.Csp;
import com.oldlie.health.medicalappointment.model.StatusCode;
import lombok.Data;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Data
public class BaseResponse implements FailedResponse<BaseResponse> {
    private int status = StatusCode.SUCCESS;
    private String message = Csp.SUCCESS;

    public void setMessage(String message) {
        try {
            this.message = URLEncoder.encode(message, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            this.message = message;
        }
    }

    @Override
    public BaseResponse setFailed(String message) {
        this.status = StatusCode.FAILED;
        setMessage(message);
        return this;
    }
}
