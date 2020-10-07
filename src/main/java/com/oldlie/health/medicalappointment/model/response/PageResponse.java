package com.oldlie.health.medicalappointment.model.response;

import lombok.*;

@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@ToString
public class PageResponse<T> extends ListResponse<T> {
    private long total;
}
