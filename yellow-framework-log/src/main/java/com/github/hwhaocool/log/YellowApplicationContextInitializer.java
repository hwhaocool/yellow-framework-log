package com.github.hwhaocool.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.Ordered;

import java.util.Comparator;
import java.util.List;

/**
 *
 * @author yellowtail
 * @since 2022/8/27 23:59
 */
public class YellowApplicationContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext>, Ordered {

    final Logger LOGGER = LoggerFactory.getLogger(YellowApplicationContextInitializer.class);

    static final List<IContextInitializer> contextInitializerList = List.of(
            new BasePackageHolderInitializer(),
            new LoggerLevelInitializer()
    );

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE - 100;
    }

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("start initialize");
        }

        // 上下文初始化的时候，先排好序，再依次触发
        contextInitializerList.stream()
                .sorted(Comparator.comparingInt(IContextInitializer::getOrder))
                .forEach(iContextInitializer -> {
                    try {
                        iContextInitializer.initialize(applicationContext);

                        if (LOGGER.isDebugEnabled()) {
                            LOGGER.debug("initialize success, {}", iContextInitializer.getClass().getSimpleName());
                        }
                    } catch (Exception e) {
                        LOGGER.error("initialize {} occur error, ", iContextInitializer.getClass().getSimpleName(), e);
                    }
                });


        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("load all initializer finish");
        }
    }


}
