package com.oldlie.health.medicalappointment.service.init.config;

import com.oldlie.health.medicalappointment.model.db.Config;

/**
 * 默认连续三次输入密码错误之后锁定5分钟
 */
public class LockedTime1 implements InitConfig {

    public final static String CONF_KEY = "lockedAccount5Min";

    private Config config;

    public LockedTime1() {
        config = new Config();
        config.setConfigGroup(ConfigGroup.SECURITY);
        config.setGroupName(ConfigGroup.SECURITY_TITLE);
        config.setConfTitle("连续登录失败次数");
        config.setConfComment("锁定账号5分钟");
        config.setConfKey(LockedTime1.CONF_KEY);
        config.setConfValue("3");
    }

    @Override
    public Config getConfig() {
        return this.config;
    }
}
