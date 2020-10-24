package com.oldlie.health.medicalappointment.model.db.repository;

import com.oldlie.health.medicalappointment.model.db.BookInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author oldlie
 * @date 2020/10/6
 */
public interface BookInfoRepository extends BaseRepository<BookInfo, Long> {
}
