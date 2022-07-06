package com.spring;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 本注解用于设置实例的作用域.
 * 默认值是单实例，即当IOC容器启动后就调用该方法创建对象放到IOC容器中，以后每次获取就是直接从容器中获取。
 * @author chenzou'quan
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Scope {
    String value() default "";
}
