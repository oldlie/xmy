package com.oldlie.health.medicalappointment.model.response;

public interface FailedResponse<T> {
    T setFailed(String message);
}
