package com.lxy.sample.config;

import com.lxy.sample.handler.DefaultEnumMsgHandler;
import com.lxy.sample.handler.EnumReturnValueHandler;
import com.lxy.sample.interfaces.EnumMsgHandler;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *     注册 枚举填充器、枚举获取handler
 * </p>
 *
 * @author xiaoyi.liu1
 * @since 2023/3/14 下午2:15
 */
@Configuration
public class ReturnValueConfig implements InitializingBean {

    private final RequestMappingHandlerAdapter requestMappingHandlerAdapter;

    @Autowired(required = false)
    private EnumMsgHandler enumMsgHandler;

    public ReturnValueConfig(RequestMappingHandlerAdapter requestMappingHandlerAdapter) {
        this.requestMappingHandlerAdapter = requestMappingHandlerAdapter;
    }

    @Override
    public void afterPropertiesSet() {
        List<HandlerMethodReturnValueHandler> originHandlers = requestMappingHandlerAdapter.getReturnValueHandlers();
        List<HandlerMethodReturnValueHandler> newHandlers = new ArrayList<>(originHandlers.size());
        for (HandlerMethodReturnValueHandler originHandler : originHandlers) {
            if (originHandler instanceof RequestResponseBodyMethodProcessor) {
                newHandlers.add(new EnumReturnValueHandler(originHandler, getEnumMsgHandler()));
            } else {
                newHandlers.add(originHandler);
            }
        }
        requestMappingHandlerAdapter.setReturnValueHandlers(newHandlers);
    }


    public EnumMsgHandler getEnumMsgHandler() {
        if (null == this.enumMsgHandler) {
            return new DefaultEnumMsgHandler();
        }
        return this.enumMsgHandler;
    }
}