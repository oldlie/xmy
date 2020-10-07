package com.oldlie.health.medicalappointment.service.init.config;

import com.oldlie.health.medicalappointment.model.db.Config;

/**
 * @author oldlie
 * @date 2020/10/3
 */
public class AllowedImageType implements InitConfig {
    public final static String CONF_KEY = "allowedImageType";

    private Config config;

    public AllowedImageType() {
        config = new Config();
        config.setConfigGroup(ConfigGroup.SYSTEM);
        config.setGroupName(ConfigGroup.SYSTEM_TITLE);
        config.setConfTitle("允许上传的文件类型");
        config.setConfComment("不区分大小写，不要包含“.”");
        config.setConfKey(CONF_KEY);
        config.setConfValue("jpg,jpeg,png,gif");
    }

    @Override
    public Config getConfig() {
        return this.config;
    }
}
