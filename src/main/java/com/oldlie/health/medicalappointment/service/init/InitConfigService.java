package com.oldlie.health.medicalappointment.service.init;

import com.oldlie.health.medicalappointment.model.db.Config;
import com.oldlie.health.medicalappointment.model.db.repository.ConfigRepository;
import com.oldlie.health.medicalappointment.service.init.config.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class InitConfigService {

    @Autowired
    private ConfigRepository configRepository;

    public void init() {
        this
                .try2CreateConfig(new LockedTime1())
                .try2CreateConfig(new LockedTime2())
                .try2CreateConfig(new UploadDir())
                .try2CreateConfig(new UploadUrl())
                .try2CreateConfig(new AkiConifg())
                .try2CreateConfig(new AksConfig())
                .try2CreateConfig(new SmsSignName())
                .try2CreateConfig(new SmsTemplateCodeConfig())
                .try2CreateConfig(new AllowedImageType())
        ;
    }

    public InitConfigService try2CreateConfig(InitConfig initConfig) {
        Config config = initConfig.getConfig();
        Optional<Config> optional = this.configRepository.findOne(
                (root, criteriaQuery, criteriaBuilder) ->
                        criteriaBuilder.equal(root.get(Config.CONF_KEY), config.getConfKey())
        );
        if (!optional.isPresent()) {
            this.configRepository.save(config);
        }
        return this;
    }
}
