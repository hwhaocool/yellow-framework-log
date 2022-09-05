package com.github.hwhaocool.log;

import org.springframework.context.ConfigurableApplicationContext;

public interface IContextInitializer {
    void initialize(ConfigurableApplicationContext applicationContext);

    int getOrder() ;


}
