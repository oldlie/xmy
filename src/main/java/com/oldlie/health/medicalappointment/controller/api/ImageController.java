package com.oldlie.health.medicalappointment.controller.api;

import com.oldlie.health.medicalappointment.exception.AppRestException;
import com.oldlie.health.medicalappointment.model.Csp;
import com.oldlie.health.medicalappointment.model.response.ItemResponse;
import com.oldlie.health.medicalappointment.service.ConfigService;
import com.oldlie.health.medicalappointment.service.FileService;
import com.oldlie.health.medicalappointment.service.init.config.AllowedImageType;
import com.oldlie.health.medicalappointment.service.init.config.UploadDir;
import com.oldlie.health.medicalappointment.service.init.config.UploadUrl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * @author oldlie
 * @date 2020/10/3
 */
@RestController
@RequestMapping("/api/image")
@Slf4j
public class ImageController {

    @Autowired
    private FileService fileService;

    @Autowired
    private ConfigService configService;

    @PostMapping("/upload")
    public ItemResponse<List<String>> image(MultipartHttpServletRequest request,
                                    @SessionAttribute("username") String username) throws AppRestException {
        ItemResponse<List<String>> response = new ItemResponse<>();
        Iterator<String> iterator = request.getFileNames();
        List<String> uploadFileList = new LinkedList<>();
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;

        MultipartFile multipartFile;

        while (iterator.hasNext()) {
            multipartFile = request.getFile(iterator.next());

            String originFileName = multipartFile.getOriginalFilename();
            if (StringUtils.isEmpty(originFileName)) {
                response.setFailed("上传了空文件");
                return response;
            }

            int lastDot = originFileName.lastIndexOf(Csp.DOT);
            if (lastDot >= 0 && lastDot < originFileName.length() - 1) {
                String suffix = originFileName.substring(lastDot + 1).toLowerCase();
                String allowedImageType = this.configService.getValue(AllowedImageType.CONF_KEY).toLowerCase();
                if (!allowedImageType.contains(suffix)) {
                    response.setFailed("仅允许上传：" + allowedImageType + "格式的文件");
                    return response;
                }
            }

            String saveFileName = this.fileService.rename(originFileName);

            String path = this.fileService.dirPath(saveFileName, username, year, month);
            File saveFile = new File(this.configService.getValue(UploadDir.CONF_KEY) + File.separator + path);
            if (!saveFile.getParentFile().exists()) {
                if (!saveFile.getParentFile().mkdirs()) {
                    response.setFailed("创建上传文件夹错误");
                    return response;
                }
            }

            try {
                multipartFile.transferTo(saveFile);
                if (!saveFile.setReadable(true, false)) {
                    log.warn("设置上传文件的访问权限失败");
                }
            } catch (IOException e) {
                e.printStackTrace();
                String error = saveFile.getAbsolutePath() + ";\r\n" + e.getMessage();
                System.out.println(saveFile.getAbsolutePath() + ";\r\n" + e.getMessage());
                log.error(e.getMessage());
                throw new AppRestException("保存上传文件到服务器时出现IO错误" + error);
            }

            uploadFileList.add(this.configService.getValue(UploadUrl.CONF_KEY)
                    + Csp.SLASH
                    + this.fileService.url(saveFileName, username, year, month));
        }
        response.setItem(uploadFileList);
        return response;
    }

    @PostMapping("/upload-file")
    public ItemResponse<String> image(@RequestParam("file") MultipartFile file) {
        ItemResponse<String> response = new ItemResponse<>();
        return response;
    }
}
