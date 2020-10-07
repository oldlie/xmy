package com.oldlie.health.medicalappointment.service.init.config;

import com.oldlie.health.medicalappointment.model.db.Config;

/**
 * @author oldlie
 * @date 2020/10/7
 */
public class SmsTemplateCodeConfig implements InitConfig {

    public final static String CONF_KEY = "smsTemplateCode";

    private Config config;

    public SmsTemplateCodeConfig() {
        config = new Config();
        config.setConfigGroup(ConfigGroup.SMS);
        config.setGroupName(ConfigGroup.SMS_TITLE);
        config.setConfTitle("TemplateCode");
        config.setConfComment("阿里云短信TemplateCode");
        config.setConfKey(CONF_KEY);
        config.setConfValue("");
    }

    @Override
    public Config getConfig() {
        return this.config;
    }
}
