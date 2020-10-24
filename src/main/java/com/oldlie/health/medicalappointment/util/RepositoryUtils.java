package com.oldlie.health.medicalappointment.util;

import com.oldlie.health.medicalappointment.model.db.repository.BaseRepository;
import com.oldlie.health.medicalappointment.model.response.ListResponse;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

/**
 * 因为这个系统默认都拥有 long 型的 id，可以简化这个类的设计
 * @author oldlie
 * @date 2020/10/24
 */
public class RepositoryUtils<T> {

    private BaseRepository<T, Long> repository;

    public RepositoryUtils(BaseRepository<T, Long> repository) {
        this.repository = repository;
    }

    public static<T> RepositoryUtils<T> getInstance(BaseRepository<T, Long> repository) {
        return new RepositoryUtils<>(repository);
    }

    public T one(long id) {
        return this.repository.findById(id).orElse(null);
    }

    public long count(Specification<T> specification) {
        return this.repository.count(specification);
    }

    public List<T> list(Specification<T> specification) {
        return this.repository.findAll(specification);
    }

    public List<T> list(Specification<T> specification, Sort sort) {
        return this.repository.findAll(specification, sort);
    }

    public ListResponse<T> listResponse(Specification<T> specification) {
        ListResponse<T> response = new ListResponse<>();
        response.setList(list(specification));
        return response;
    }
}
