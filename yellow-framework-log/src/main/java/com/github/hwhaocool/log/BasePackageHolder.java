package com.github.hwhaocool.log;

import java.util.List;

/**
 * @author yellowtail
 * @since 2022/8/30 22:06
 */
public class BasePackageHolder {

    static List<String> basePackages;

    public static List<String> getBasePackages() {
        return basePackages;
    }

    public static void setBasePackages(List<String> basePackages) {
        BasePackageHolder.basePackages = basePackages;
    }
}
