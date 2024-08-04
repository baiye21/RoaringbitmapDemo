package com.demo.roaringbitmap.annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @Description:
 * @Author: baiye <baiye_21@163.com>
 * @CreateTime: 2024/7/3
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface TagProcessorSpringBean {
    /**
     * 注入的spring bean name
     */
    String beanName() default "";
}
