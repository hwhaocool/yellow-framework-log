package org.example.hwhaocool.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

/**
 * 启动的时候打印一个日志
 * @author yellowtail
 * @since 2022/9/5 21:15
 */
@Component
public class StartupComponent implements InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(StartupComponent.class);

    @Override
    public void afterPropertiesSet() throws Exception {
        LOGGER.debug("start up");
    }
}
