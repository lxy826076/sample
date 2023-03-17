package com.lxy.sample.annotation;

import com.lxy.sample.handler.EnumScannerRegistrar;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;


/**
 * <p>
 * 允许枚举填充功能枚举<br>
 * </p>
 *
 * @author xiaoyi.liu1
 * @since 2023/3/15 下午4:16
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(value = EnumScannerRegistrar.class)
public @interface EnableEnumFill {
    String[] basePackages() default {};
}