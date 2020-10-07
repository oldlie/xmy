package com.oldlie.health.medicalappointment.service.init.config;

import com.oldlie.health.medicalappointment.model.db.Config;

/**
 * @author oldlie
 * @date 2020/10/3
 */
public class UploadUrl implements InitConfig {

    public final static String CONF_KEY = "uploadUrl";

    private Config config;

    public UploadUrl() {
        config = new Config();
        config.setConfigGroup(ConfigGroup.SYSTEM);
        config.setGroupName(ConfigGroup.SYSTEM_TITLE);
        config.setConfTitle("上传文件访问路径");
        config.setConfComment("最好写绝对路径");
        config.setConfKey(CONF_KEY);
        config.setConfValue("http://localhost/ma/res");
    }

    @Override
    public Config getConfig() {
        return this.config;
    }
}
