package com.github.hwhaocool.log;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.impl.LocationAware;
import org.apache.logging.log4j.core.pattern.ConverterKeys;
import org.apache.logging.log4j.core.pattern.LogEventPatternConverter;

/**
 * @author yellowtail
 * @since 2022/8/27 23:16
 */
@Plugin(
        name = "LineLocationPatternConverter",
        category = "Converter"
)
@ConverterKeys({"pc", "prettyClass"})
public class PrettyClassPatternConverter extends LogEventPatternConverter implements LocationAware {

    protected PrettyClassPatternConverter(String name, String style) {
        super(name, style);
    }

    private PrettyClassPatternConverter(final String[] options) {
        this("PrettyClass", "prettyClass");
    }

    // 这个是被触发的得到实例的方法
    public static PrettyClassPatternConverter newInstance(final String[] options) {
        return new PrettyClassPatternConverter(options);
    }

    @Override
    public boolean requiresLocation() {
        return true;
    }

    @Override
    public void format(LogEvent event, StringBuilder toAppendTo) {

    }
}
