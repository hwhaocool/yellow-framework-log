yellow-framework-log
---

## 子工程

|name|desc|  
|---|---|
|yellow-framework-log|日志组件|
|web-default|使用spring boot 默认日志的web项目|
|web-with-log|使用自定义日志组件的web项目|

## 使用

你可以使用我的 jar 包，也可以把类复制过去放到你自己的项目中

maven 依赖
```
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
    <exclusions>
        <!-- 排除默认的日志组件 -->
        <exclusion>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-logging</artifactId>
        </exclusion>
    </exclusions>
</dependency>

<!-- 引入springboot 的 log4j2 -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-log4j2</artifactId>
</dependency>

<!-- 引入我们的日志组件 -->
<dependency>
    <groupId>com.github.hwhaocool</groupId>
    <artifactId>yellow-framework-log</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>
```