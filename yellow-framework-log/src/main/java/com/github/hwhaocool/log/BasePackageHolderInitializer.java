package com.github.hwhaocool.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * @author yellowtail
 * @since 2022/8/30 21:54
 */
public class BasePackageHolderInitializer implements IContextInitializer {

    private static final Logger LOGGER = LoggerFactory.getLogger(BasePackageHolderInitializer.class);

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {

        // 新建扫描器，不使用默认的过滤器
        ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);

        // 新增一个过滤器：找到带有注解 SpringBootApplication 的类
        scanner.addIncludeFilter(new AnnotationTypeFilter(SpringBootApplication.class));

        // 扫描范围是 所有
        Set<BeanDefinition> candidateComponents = scanner.findCandidateComponents("");

        for (BeanDefinition candidateComponent : candidateComponents) {
            Class<?> clazz;
            try {
                clazz = applicationContext.getClassLoader().loadClass(candidateComponent.getBeanClassName());
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }

            SpringBootApplication annotation = AnnotationUtils.findAnnotation(clazz, SpringBootApplication.class);
            if (null == annotation) {
                continue;
            }

            List<String> list = null;
            String[] strings = annotation.scanBasePackages();

            if (null == strings || strings.length == 0) {
                // 没有写注解，就用类的包名
                String packageName = clazz.getPackageName();

                list = Collections.singletonList(packageName);
            } else {
                list = Arrays.asList(strings);
            }

            LOGGER.info("basePackage {}", list);

            BasePackageHolder.setBasePackages(list);
        }
    }

}
