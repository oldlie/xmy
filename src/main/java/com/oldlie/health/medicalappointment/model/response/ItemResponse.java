package com.oldlie.health.medicalappointment.model.response;

import lombok.*;

@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@ToString
public class ItemResponse<T> extends BaseResponse{
    private T item;
}
