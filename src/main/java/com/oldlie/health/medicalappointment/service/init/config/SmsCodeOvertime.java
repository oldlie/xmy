package com.oldlie.health.medicalappointment.service.init.config;

import com.oldlie.health.medicalappointment.model.db.Config;

/**
 * 短信超时时间，分钟计
 * @author oldlie
 * @date 2020/10/4
 */
public class SmsCodeOvertime implements InitConfig{
    public final static String CONF_KEY = "smsCodeOvertime";

    private Config config;

    public SmsCodeOvertime() {
        config = new Config();
        config.setConfigGroup(ConfigGroup.SMS);
        config.setGroupName(ConfigGroup.SMS_TITLE);
        config.setConfTitle("短信超时时间设置");
        config.setConfComment("分钟计算");
        config.setConfKey(CONF_KEY);
        config.setConfValue("5");
    }

    @Override
    public Config getConfig() {
        return this.config;
    }
}
