package com.oldlie.health.medicalappointment.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;

/**
 * @author chenlie
 */
public class Tools {

    public static final String DESC = "desc";
    public static final String ASC = "asc";

    public static Sort sort(String orderBy, String order) {
        return order == null || order.toLowerCase().equals("desc") ?
                Sort.by(Sort.Direction.DESC, orderBy) : Sort.by(Sort.Direction.ASC, orderBy);
    }

    /**
     * Get Pageable object
     *
     * @param page    page index start with 1
     * @param size    page size
     * @param orderBy field
     * @param order   direct
     * @return pageable object
     */
    public static Pageable pageable(int page, int size, String orderBy, String order) {
        int start = page <= 1 ? 0 : page - 1;
        return PageRequest.of(start, size, Tools.sort(orderBy, order));
    }

    /**
     * Get list by id desc
     *
     * @param page page index
     * @param size page size
     * @return pageable
     */
    public static Pageable pageable(int page, int size) {
        return pageable(page, size, "id", "desc");
    }

    public static long unboxLong(Long l) {
        return l == null ? 0 : l.longValue();
    }

    public static long getYmd() {
        return getYmd(null);
    }

    public static long getYmd(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            localDateTime = LocalDateTime.now();
        }
        return localDateTime.getYear() * 10000 + localDateTime.getMonthValue() * 100 + localDateTime.getDayOfMonth();
    }
}
