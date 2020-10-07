package com.oldlie.health.medicalappointment.util;

import com.oldlie.health.medicalappointment.model.db.BaseId;
import com.oldlie.health.medicalappointment.model.response.BaseResponse;
import com.oldlie.health.medicalappointment.model.response.ItemResponse;
import com.oldlie.health.medicalappointment.model.response.ListResponse;
import com.oldlie.health.medicalappointment.model.response.PageResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.util.Assert;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

/**
 * @author oldlie
 * @date 2020/9/26
 */
public class ServiceUtil<T extends BaseId, R extends JpaRepository<T, Long>> {

    R repository;
    T target;

    public ServiceUtil(R repository) {
        this.repository = repository;
        this.target = null;
    }

    public ServiceUtil(T target, R repository) {
        this.target = target;
        this.repository = repository;
    }


    public ItemResponse<Long> store(T source) {
        Assert.notNull(target, "target is null");
        ItemResponse<Long> response = new ItemResponse<>();
        if (source.getId() != null && source.getId() > 0) {
            Optional<T> optional = repository.findById(source.getId());
            if (!optional.isPresent()) {
                response.setFailed("要修改的项目不存在了，请刷新重试。");
                return response;
            }
            target = optional.get();
        }
        ObjectCopy<T> copy = new ObjectCopy<>();
        target = copy.copyValue2Entity(source, target);
        target = repository.save(target);
        response.setItem(target.getId());
        return response;
    }

    public ItemResponse<T> one(long id) {
        ItemResponse<T> response = new ItemResponse<>();
        T item = this.repository.findById(id).orElse(null);
        if (item == null) {
            response.setFailed("没有找到指定的信息");
            return response;
        }
        response.setItem(item);
        return response;
    }

    public BaseResponse delete(long id) {
        BaseResponse response = new BaseResponse();
        this.repository.findById(id).ifPresent(x -> this.repository.delete(x));
        return response;
    }

    public ListResponse<T> list() {
        ListResponse<T> response = new ListResponse<>();
        List<T> list = this.repository.findAll();
        response.setList(list);
        return response;
    }

    public ListResponse<T> list(Sort sort) {
        ListResponse<T> response = new ListResponse<>();
        List<T> list = this.repository.findAll(sort);
        response.setList(list);
        return response;
    }

    public PageResponse<T> page(Pageable pageable) {
        PageResponse<T> response = new PageResponse<>();
        Page<T> page = this.repository.findAll(pageable);
        response.setList(page.getContent());
        response.setTotal(page.getTotalElements());
        return response;
    }
}
