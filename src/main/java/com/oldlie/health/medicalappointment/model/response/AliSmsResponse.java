package com.oldlie.health.medicalappointment.model.response;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

/**
 * @author oldlie
 * @date 2020/10/18
 */
@Data
public class AliSmsResponse {
    @SerializedName("Message")
    private String message;
    @SerializedName("RequestId")
    private String requestId;
    @SerializedName("Code")
    private String code;
}
