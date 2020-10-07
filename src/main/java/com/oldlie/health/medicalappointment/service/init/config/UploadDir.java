package com.oldlie.health.medicalappointment.service.init.config;

import com.oldlie.health.medicalappointment.model.db.Config;

/**
 * @author oldlie
 * @date 2020/10/3
 */
public class UploadDir implements InitConfig {

    public final static String CONF_KEY = "uploadDir";

    private Config config;

    public UploadDir() {
        config = new Config();
        config.setConfigGroup(ConfigGroup.SYSTEM);
        config.setGroupName(ConfigGroup.SYSTEM_TITLE);
        config.setConfTitle("上传文件的文件夹");
        config.setConfComment("最好写绝对路径");
        config.setConfKey(CONF_KEY);
        config.setConfValue("d:/nginx-1.18.0/html/ma/res");
    }

    @Override
    public Config getConfig() {
        return this.config;
    }
}
