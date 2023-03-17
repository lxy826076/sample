package com.lxy.sample.interfaces;

/**
 * <p>
 * 枚举消息获取顶层接口
 * </p>
 *
 * @author xiaoyi.liu1
 * @since 2023/3/15 下午4:13
 */
public interface EnumMsgHandler {
    /**
     * 获取枚举国际化
     *
     * @param keyName
     * @return
     */
    String getMsgByCode(String keyName);
}
