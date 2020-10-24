package com.oldlie.health.medicalappointment.service;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.oldlie.health.medicalappointment.model.Csp;
import com.oldlie.health.medicalappointment.model.ExportBookInfo;
import com.oldlie.health.medicalappointment.model.db.BookInfo;
import com.oldlie.health.medicalappointment.model.db.repository.BookInfoRepository;
import com.oldlie.health.medicalappointment.model.response.ItemResponse;
import com.oldlie.health.medicalappointment.util.RepositoryUtils;
import com.oldlie.health.medicalappointment.util.Tools;
import com.oldlie.health.medicalappointment.util.asynctask.AsyncTask;
import com.oldlie.health.medicalappointment.util.asynctask.AsyncTaskUtils;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.File;
import java.time.LocalDateTime;
import java.util.*;

/**
 * @author oldlie
 * @date 2020/10/3
 */
@Service
@Log4j2
public class FileService {

    @Autowired
    private AsyncTaskUtils asyncTaskUtils;

    @Autowired
    private BookInfoRepository bookInfoRepository;

    private long maxExportLimit = 1000;


    public String dirPath(String fileName, String username, int year, int month) {
        StringBuilder builder = new StringBuilder(Csp.CAPACITY_128);
        String pathPrefix = DigestUtils.md5Hex(username).substring(0, 2);
        builder.append(pathPrefix).append(File.separator)
                .append(username).append(File.separator)
                .append(year).append(File.separator)
                .append(month).append(File.separator)
                .append(fileName);
        return builder.toString();
    }

    public String url(String fileName, String username, int year, int month) {
        StringBuilder builder = new StringBuilder(Csp.CAPACITY_128);
        String pathPrefix = DigestUtils.md5Hex(username).substring(0, 2);
        builder.append(pathPrefix).append(Csp.SLASH)
                .append(username).append(Csp.SLASH)
                .append(year).append(Csp.SLASH)
                .append(month).append(Csp.SLASH)
                .append(fileName);
        return builder.toString();
    }

    public String rename(String fileName) {
        int lastDot = fileName.lastIndexOf(Csp.DOT);
        String suffix = ".jpg";
        if (lastDot >= 0) {
            suffix = fileName.substring(lastDot);
        }
        return UUID.randomUUID().toString() + suffix;
    }

    private int vc = 0;

    private synchronized int getVc() {
        if (vc >= 1000) {
            vc = 0;
        }
        return vc++;
    }

    private class ExportBookInfoQuery implements Specification<BookInfo> {

        private final long did;
        private final int startYmd;
        private final int endYmd;
        private final int canceled;

        public ExportBookInfoQuery(long did, int start, int end, int canceled) {
            this.did = did;
            this.startYmd = start;
            this.endYmd = end;
            this.canceled = canceled;
        }

        @Override
        public Predicate toPredicate(Root<BookInfo> root,
                                     CriteriaQuery<?> criteriaQuery,
                                     CriteriaBuilder criteriaBuilder) {
            List<Predicate> list = new LinkedList<>();
            if (this.did > 0) {
                list.add(criteriaBuilder.equal(root.get(BookInfo.DID), did));
            }
            if (this.canceled >= 0) {
                list.add(criteriaBuilder.equal(root.get(BookInfo.CANCELED), canceled));
            }
            list.add(criteriaBuilder.between(root.get(BookInfo.YMD), startYmd, endYmd));
            return criteriaBuilder.and(list.toArray(new Predicate[0]));
        }
    }

    public ItemResponse<Map<String, Object>> exportBookInfo(String username,
                                                            long did,
                                                            int startYmd,
                                                            int endYmd,
                                                            int canceled) {
        ItemResponse<Map<String, Object>> response = new ItemResponse<>();
        RepositoryUtils<BookInfo> utils = RepositoryUtils.getInstance(this.bookInfoRepository);

        ExportBookInfoQuery query = new ExportBookInfoQuery(did, startYmd, endYmd, canceled);
        long count = utils.count(query);
        if (count >= maxExportLimit) {
            return response.setFailed("时间范围内的预约记录超过" + maxExportLimit + "条了，请缩小时间范围");
        }
        LocalDateTime localDateTime = LocalDateTime.now();
        String name = "预约表_" + getVc() + ".xlsx";
        int year = localDateTime.getYear();
        int month = localDateTime.getMonthValue();
        String fileName = dirPath(name, username, year, month);
        List<BookInfo> list = utils.list(query, Tools.sort(BookInfo.YMD, Tools.ASC));

        AsyncTask task = this.asyncTaskUtils.buildTask("导出预约列表", "", () -> {
            ExcelWriter excelWriter = null;
            try {
                excelWriter = EasyExcel.write(fileName, ExportBookInfo.class).build();
                WriteSheet writeSheet = EasyExcel.writerSheet("预约记录").build();
                excelWriter.write(list, writeSheet);
            } catch (Exception e) {
                log.error(e.getMessage());
            } finally {
                // 千万别忘记finish 会帮忙关闭流
                if (excelWriter != null) {
                    excelWriter.finish();
                }
            }
        });
        this.asyncTaskUtils.start(task);
        Map<String, Object> map = new HashMap<>(Csp.CAPACITY_8);
        map.put("task", task);
        map.put("url", url(fileName, username, year, month));
        return response;
    }

    public ItemResponse<AsyncTask> taskStatus(long id) {
        AsyncTask asyncTask = this.asyncTaskUtils.info(id);
        ItemResponse<AsyncTask> response = new ItemResponse<>();
        response.setItem(asyncTask);
        return response;
    }
}
