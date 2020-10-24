package com.oldlie.health.medicalappointment.model.response;

import lombok.*;

/**
 * @author oldlie
 * @param <T>
 */
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@ToString
public class ItemResponse<T> extends BaseResponse{
    private T item;

    @Override
    public ItemResponse<T> setFailed(String message) {
        super.setFailed(message);
        return this;
    }
}
