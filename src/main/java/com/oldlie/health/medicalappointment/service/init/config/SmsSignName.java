package com.oldlie.health.medicalappointment.service.init.config;

import com.oldlie.health.medicalappointment.model.db.Config;

/**
 * @author oldlie
 * @date 2020/10/7
 */
public class SmsSignName implements InitConfig {

    public final static String CONF_KEY = "smsSignName";

    private Config config;

    public SmsSignName() {
        config = new Config();
        config.setConfigGroup(ConfigGroup.SMS);
        config.setGroupName(ConfigGroup.SMS_TITLE);
        config.setConfTitle("SignName");
        config.setConfComment("阿里云短信SignName");
        config.setConfKey(CONF_KEY);
        config.setConfValue("");
    }

    @Override
    public Config getConfig() {
        return this.config;
    }
}
