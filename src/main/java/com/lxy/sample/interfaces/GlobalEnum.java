package com.lxy.sample.interfaces;

/**
 * <p>
 * 枚举顶层接口，该接口的实现类可实现返回前端时自动填充
 * </p>
 *
 * @author xiaoyi.liu1
 * @since 2023/3/14 下午2:15
 */
public interface GlobalEnum {

    /**
     * 获取枚举 code
     * @return
     */
    Integer getCode();

    /**
     * 获取枚举描述，可实现国际化
     * @return
     */
    String getDesc();
}
