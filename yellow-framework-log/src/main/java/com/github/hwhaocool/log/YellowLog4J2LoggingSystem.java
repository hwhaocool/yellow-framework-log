package com.github.hwhaocool.web.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.boot.context.properties.bind.Bindable;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.boot.logging.LogFile;
import org.springframework.boot.logging.LoggingInitializationContext;
import org.springframework.boot.logging.LoggingSystem;
import org.springframework.boot.logging.LoggingSystemFactory;
import org.springframework.boot.logging.log4j2.Log4J2LoggingSystem;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.util.ClassUtils;

import java.util.Collections;
import java.util.List;

/**
 * @author yellowtail
 * @since 2022/8/21 17:48
 */
public class YellowLog4J2LoggingSystem extends Log4J2LoggingSystem {

    private static final Logger LOGGER = LoggerFactory.getLogger(YellowLog4J2LoggingSystem.class);

    public YellowLog4J2LoggingSystem(ClassLoader classLoader) {
        super(classLoader);
    }

    @Override
    protected void loadDefaults(LoggingInitializationContext initializationContext, LogFile logFile) {
        if (logFile != null) {
            // 配置了日志输出文件
            loadConfiguration(getPackagedConfigFile("log4j2-file.xml"), logFile, getOverrides(initializationContext));
        }
        else {
            // 什么都没有配置

            // 1. 得到当前的环境(spring.profile.active)
            ProfileEnum profile = getProfile(initializationContext.getEnvironment());

            // 2. 当前环境应该加载的配置文件
            String logConfigFile = getConfigFile(profile);

            // 3. local dev 激活彩色console日志
            enableColor(profile);

            // 4. 开启异步
            enableAsync();

            // 5. 加载
            loadConfiguration(getPackagedConfigFile(logConfigFile), logFile, getOverrides(initializationContext));
            LOGGER.info("load {}", logConfigFile);

            // 6. 自适应包名
            autofixPackage();
        }
    }

    private void autofixPackage() {
        // TODO: 待实现
    }

    /**
     * 开启异步
     */
    private void enableAsync() {
        System.setProperty("log4j2.contextSelector", "org.apache.logging.log4j.core.async.AsyncLoggerContextSelector");
    }

    /**
     * 激活彩色日志
     * @param profile
     */
    private void enableColor(ProfileEnum profile) {
        if (ProfileEnum.LOCAL == profile || ProfileEnum.DEV == profile) {
            System.setProperty("log4j.skipJansi", "false");
        }
    }

    /**
     * 当前环境应该加载的配置文件
     * @param profile
     * @return
     */
    private String getConfigFile(ProfileEnum profile) {
        switch (profile) {
            case DEV:
                return "log4j2-dev.xml";
            case PROD:
                return "log4j2-prod.xml";
            case LOCAL:
            default:
                return "log4j2-local.xml";
        }
    }

    /**
     * 得到当前的环境(spring.profile.active)
     * @param environment
     * @return
     */
    private ProfileEnum getProfile(Environment environment) {
        String[] activeProfiles = environment.getActiveProfiles();

        if (null == activeProfiles || activeProfiles.length == 0) {
            return ProfileEnum.LOCAL;
        }

        switch (activeProfiles[0]) {
            case "local":
                return ProfileEnum.LOCAL;
            case "dev":
            case "develop":
                return ProfileEnum.DEV;
            case "prod":
            case "prd":
            case "product":
            default:
                return ProfileEnum.PROD;
        }
    }

    // 直接复制的，没有做任何改变
    private List<String> getOverrides(LoggingInitializationContext initializationContext) {
        BindResult<List<String>> overrides = Binder.get(initializationContext.getEnvironment())
                .bind("logging.log4j2.config.override", Bindable.listOf(String.class));
        return overrides.orElse(Collections.emptyList());
    }

    static enum ProfileEnum {
        /**
         * 本地debug环境
         */
        LOCAL,
        /**
         * dev环境
         */
        DEV,
        /**
         * 生产 环境
         */
        PROD
    }

    /**
     * 初始化YellowLog4J2LoggingSystem
     * order 调整一下，稍微加一点优先级
     * @author yellowtail
     */
    @Order(Ordered.LOWEST_PRECEDENCE-100)
    public static class Factory implements LoggingSystemFactory {

        private static final boolean PRESENT = ClassUtils
                .isPresent("org.apache.logging.log4j.core.impl.Log4jContextFactory", Log4J2LoggingSystem.Factory.class.getClassLoader());

        @Override
        public LoggingSystem getLoggingSystem(ClassLoader classLoader) {
            if (PRESENT) {
                return new YellowLog4J2LoggingSystem(classLoader);
            }
            return null;
        }

    }
}
