package com.oldlie.health.medicalappointment.service;

import com.oldlie.health.medicalappointment.model.Csp;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.UUID;

/**
 * @author oldlie
 * @date 2020/10/3
 */
@Service
@Slf4j
public class FileService {

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
}
