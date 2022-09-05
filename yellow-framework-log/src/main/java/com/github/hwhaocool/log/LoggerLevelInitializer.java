package com.github.hwhaocool.log;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.Configurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * 对业务日志进行自适应初始化
 * @author yellowtail
 * @since 2022/8/30 21:54
 */
public class LoggerLevelInitializer implements IContextInitializer {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggerLevelInitializer.class);

    @Override
    public int getOrder() {
        return 1;
    }

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        YellowLog4J2LoggingSystem.ProfileEnum profileEnum = ProfileHolder.getProfileEnum();

        if (YellowLog4J2LoggingSystem.ProfileEnum.LOCAL == profileEnum || YellowLog4J2LoggingSystem.ProfileEnum.DEV == profileEnum) {

            for (String basePackage : BasePackageHolder.getBasePackages()) {
                if (basePackage.startsWith("com.github.hwhaocool")) {
                    // 已经配置过了
                    continue;
                }

                Configurator.setLevel(basePackage, Level.toLevel("debug"));

                LOGGER.info("set debug level for {}", basePackage);
            }
        }
    }


}
