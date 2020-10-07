package com.oldlie.health.medicalappointment.service.init.config;

import com.oldlie.health.medicalappointment.model.db.Config;

/**
 * 连续输入密码6次之后锁定账号24小时
 */
public class LockedTime2 implements InitConfig {

    public final static String CONF_KEY = "lockedAccount24Hour";

    private Config config;

    public LockedTime2() {
        config = new Config();
        config.setConfigGroup(ConfigGroup.SECURITY);
        config.setGroupName(ConfigGroup.SECURITY_TITLE);
        config.setConfTitle("连续登录失败次数");
        config.setConfComment("锁定账号5分钟");
        config.setConfKey(CONF_KEY);
        config.setConfValue("10");
    }

    @Override
    public Config getConfig() {
        return this.config;
    }
}
