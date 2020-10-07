package com.oldlie.health.medicalappointment.service.init.config;

import com.oldlie.health.medicalappointment.model.db.Config;

/**
 * @author oldlie
 * @date 2020/10/7
 */
public class AksConfig implements InitConfig {

    public final static String CONF_KEY = "accessKeySecret";

    private Config config;

    public AksConfig() {
        config = new Config();
        config.setConfigGroup(ConfigGroup.SECURITY);
        config.setGroupName(ConfigGroup.SECURITY_TITLE);
        config.setConfTitle("AccessKey Secret");
        config.setConfComment("阿里云");
        config.setConfKey(CONF_KEY);
        config.setConfValue("");
    }

    @Override
    public Config getConfig() {
        return this.config;
    }
}
