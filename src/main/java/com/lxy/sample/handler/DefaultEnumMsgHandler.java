package com.lxy.sample.handler;

import com.lxy.sample.interfaces.EnumMsgHandler;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Locale;


/**
 * <p>
 * 默认实现枚举读取，只读取定义的枚举，不支持国际化<br>
 * 如果需要自定义，实现EnumMsgHandler并注入spring
 * </p>
 *
 * @author xiaoyi.liu1
 * @since 2023/3/15 下午4:16
 */
public class DefaultEnumMsgHandler implements EnumMsgHandler {

    @Override
    public String getMsgByCode(String keyName) {
        // 动态获取当前语言
        Locale locale = LocaleContextHolder.getLocale();
        return EnumScannerRegistrar.table.get(keyName, locale.getLanguage());
    }

}
