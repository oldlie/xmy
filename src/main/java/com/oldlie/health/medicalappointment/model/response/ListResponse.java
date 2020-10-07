package com.oldlie.health.medicalappointment.model.response;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@ToString
public class ListResponse<T> extends BaseResponse {
    private List<T> list;

}
