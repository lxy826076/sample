package com.lxy.sample.annotation;

import java.lang.annotation.*;


/**
 * <p>
 * 枚举注解，在返回的实体类中标记
 * </p>
 *
 * @author xiaoyi.liu1
 * @since 2023/3/15 下午4:16
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface EnumValue {
    Class<?> enumType() default Void.class;
}