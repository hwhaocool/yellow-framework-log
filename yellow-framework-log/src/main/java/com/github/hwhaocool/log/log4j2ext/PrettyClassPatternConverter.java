package com.github.hwhaocool.log.log4j2ext;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.impl.LocationAware;
import org.apache.logging.log4j.core.pattern.ConverterKeys;
import org.apache.logging.log4j.core.pattern.LogEventPatternConverter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * prettyClass 的pattern 转换器
 * @author yellowtail
 * @since 2022/8/27 23:16
 */
@Plugin(
        name = "PrettyClassPatternConverter",
        category = "Converter"
)
@ConverterKeys({"pc", "prettyClass"})
public class PrettyClassPatternConverter extends LogEventPatternConverter implements LocationAware {

    private final MyAbbreviator abbreviator;

    protected PrettyClassPatternConverter(String name, String style, final String[] options) {
        super(name, style);

        if (options != null && options.length > 0) {
            abbreviator = new MyAbbreviator(options[0]);
        } else {
            abbreviator = new MyAbbreviator(null);
        }
    }

    private PrettyClassPatternConverter(final String[] options) {
        this("PrettyClass", "prettyClass", options);
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

//        event.get

        // 格式： className.methodName():line
        // 其中 className 根据长度变长变短

        String className = event.getLoggerName();

        final StackTraceElement element = event.getSource();

        if (null == element) {
            abbreviator.abbreviate(className, toAppendTo);
            return;
        }

        abbreviator.abbreviate(className, element, toAppendTo);


    }

    static class MyAbbreviator {

        Integer lengthLimit;

        public MyAbbreviator(String option) {
            if (null != option) {
                this.lengthLimit = Integer.parseInt(option.trim());
            }
        }

        public void abbreviate(final String original, final StringBuilder destination) {
            abbreviate(original, null, null, destination);
        }

        public void abbreviate(String className, StackTraceElement element, StringBuilder toAppendTo) {
            int lineNumber = element.getLineNumber();
            String methodName = element.getMethodName();

            abbreviate(className, methodName, lineNumber, toAppendTo);
        }

        private void abbreviate(String className, String methodName, Integer lineNumber, StringBuilder toAppendTo) {

            if (null == lengthLimit) {
                // 无限制，全部输出
                outputAll(className, methodName, lineNumber, toAppendTo);
                return;
            }

            outputWithLimit(className, methodName, lineNumber, toAppendTo);
        }

        private void outputAll(String className, String methodName, Integer lineNumber, StringBuilder toAppendTo) {
            if (null == methodName) {
                toAppendTo.append(className);
                return;
            }

            toAppendTo.append(String.format("%s.%s():%d", className, methodName, lineNumber));
        }

        private void outputWithLimit(String className, String methodName, Integer lineNumber, StringBuilder toAppendTo) {

            String finalClassName = new ClassNameAbbreviator(lengthLimit)
                    .methodAndLine(methodName, lineNumber)
                    .className(className);

            toAppendTo.append(finalClassName);
        }
    }

    static class ClassNameAbbreviator {

        int totalLength;

        int limit;

        String methodName;

        Integer lineNumber;

        public ClassNameAbbreviator(int limit) {
            this.totalLength = limit;
            this.limit = limit;
        }

        public ClassNameAbbreviator methodAndLine(String methodName, Integer lineNumber) {

            this.methodName = methodName;
            this.lineNumber = lineNumber;

            if (null == methodName) {
                return this;
            }

            int useLength = methodName.length() + lineNumber.toString().length() + 4;

            limit -= useLength;

            return this;
        }

        public String className(String className) {

            String fixClassNameContent = fixClassNameContent(className);

            if (null == methodName) {
                return fixLength(fixClassNameContent);
            }

            return fixLength(String.format("%s.%s():%d", fixClassNameContent, methodName, lineNumber));
        }

        /**
         *  长度不够，空格来凑
         * @param content
         * @return String
         * @author: huangwei
         * @date: 2022/8/18
         */
        private String fixLength(String content) {

            if (content.length() >= totalLength) {
                return content;
            }

            int gap = totalLength - content.length();
            return content + IntStream.rangeClosed(1, gap)
                    .mapToObj(k -> " ")
                    .collect(Collectors.joining());
        }

        public String fixClassNameContent(String className) {

            // 没有超限，直接输出
            if (className.length() <= limit) {
                return className;
            }

            int index = className.lastIndexOf(46);

            // 没有点，无法截断，直接输出
            if (-1 == index) {
                return className;
            }

            // 倒数第一层, 肯定要输出的
            // name 是带点号的 类名
            String name = className.substring(index);

            // 倒数其它层，也就是包名， 不一定要输出，尽可能输出，不够就只输出第一个字母
            String packageString = className.substring(0, index);

            String[] strings = packageString.split("\\.");
            int dotCount = strings.length-1;

            int actualLimit = limit - name.length() - dotCount;

            List<String> result = new ArrayList<>();

            boolean simpleOutput = false;
            for (int j = strings.length - 1; j >= 0; j--) {
                String string = strings[j];

                if (simpleOutput) {
                    result.add(string.substring(0,1));
                    actualLimit--;
                    continue;
                }

                // j 是还要保留几位的意思，刚好等于索引，就拿来用了
                if (string.length() + j > actualLimit) {
                    // 已经超了

                    simpleOutput = true;

                    result.add(string.substring(0,1));
                    actualLimit--;
                    continue;
                }

                // 没有超，那就输出
                result.add(string);
                actualLimit -= string.length();
            }

            // 倒叙把 result 输出一下
            String[] reverseArray = result.toArray(new String[]{});
            CollectionUtils.reverseArray(reverseArray);

            // 再接上必须输出的 类名
            return String.join(".", reverseArray) + name;

        }
    }
}
