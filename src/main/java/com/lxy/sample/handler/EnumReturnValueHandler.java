package com.lxy.sample.handler;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.lxy.sample.annotation.EnumValue;
import com.lxy.sample.bean.DynamicBeanUtils;
import com.lxy.sample.interfaces.EnumMsgHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;


/**
 * <p>
 * 对返回的对象进行枚举填充，所有加了ResponseBody的接口都可以进行拦截处理<br>
 * 目前支持的返回实体类有：
 * <ul>
 *     <li>IPage<DTO></li>
 *     <li>List<DTO></li>
 *     <li>DTO</li>
 * </ul>
 * 循环嵌套暂未实现
 * </p>
 *
 * @author xiaoyi.liu1
 * @since 2023/3/14 下午2:15
 */
@Slf4j
public class EnumReturnValueHandler implements HandlerMethodReturnValueHandler {

    private EnumMsgHandler enumMsgHandler;

    private final HandlerMethodReturnValueHandler handler;

    public EnumReturnValueHandler(HandlerMethodReturnValueHandler handler, EnumMsgHandler enumMsgHandler) {
        this.handler = handler;
        this.enumMsgHandler = enumMsgHandler;
    }

    @Override
    public boolean supportsReturnType(MethodParameter returnType) {
        return (AnnotatedElementUtils.hasAnnotation(returnType.getContainingClass(), ResponseBody.class) ||
                returnType.hasMethodAnnotation(ResponseBody.class));
    }

    @Override
    public void handleReturnValue(Object returnValue, MethodParameter returnType,
                                  ModelAndViewContainer mavContainer, NativeWebRequest webRequest) throws Exception {
        if (null != returnValue) {
            dealEnum(returnValue);
        }

        handler.handleReturnValue(returnValue, returnType, mavContainer, webRequest);
    }

    /**
     * 处理属性值枚举填充
     *
     * @param returnValue 返回的实体
     */
    private void dealEnum(Object returnValue) {
        try {
            if (null == returnValue) {
                return;
            }
            Class<?> beanClazz = returnValue.getClass();
            Method getCodeMethod = beanClazz.getMethod("getCode");
            Integer code = (Integer) getCodeMethod.invoke(returnValue);
            if (code.equals(100000)) {
                // 目前只支持 IPage<DTO>,List<DTO>,DTO 三种返回值的处理，DTO内再嵌套DTO暂不支持
                Method getNameMethod = beanClazz.getMethod("getData");
                Object datas = getNameMethod.invoke(returnValue);
                if (datas instanceof IPage) {
                    if (((IPage<?>) datas).getSize() > 0) {
                        List<Object> records = ((IPage<Object>) datas).getRecords();
                        dealCollection(records);
                    }
                    return;
                } else if (datas instanceof Collection) {
                    dealCollection(datas);
                    return;
                }
                datas = dealObject(datas, new AtomicBoolean(Boolean.FALSE));
                Method setDataMethod = beanClazz.getMethod("setData", Object.class);
                setDataMethod.invoke(returnValue, datas);
            }
        } catch (Exception e) {
            log.error("process enum exception,msg:", e);
        }
    }

    /**
     * 处理集合数据
     *
     * @param datas 集合对象
     */
    private void dealCollection(Object datas) {
        AtomicBoolean annotationFlag = new AtomicBoolean(Boolean.FALSE);
        Collection records = ((Collection) datas);
        Iterator iterator = records.iterator();
        List list = new ArrayList(records.size());
        while (iterator.hasNext()) {
            Object data = iterator.next();
            list.add(dealObject(data, annotationFlag));
            if (!annotationFlag.get()) {
                break;
            }

        }
        if (annotationFlag.get()) {
            records.clear();
            records.addAll(list);
        }
    }

    /**
     * 实体类处理
     *
     * @param data           实体类
     * @param annotationFlag 判断实体类是否加注解
     * @return 填充说明字段后的实体
     */

    private Object dealObject(Object data, AtomicBoolean annotationFlag) {
        // 获取 "属性变量" 上的注解的值
        Field[] fields = data.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(EnumValue.class)) {
                annotationFlag.set(Boolean.TRUE);
                // 获取注解的值
                EnumValue enumValue = field.getAnnotation(EnumValue.class);
                String enumType = enumValue.enumType().getName();
                String shortName = enumType.substring(enumType.lastIndexOf(".") + 1);
                // 获取字段的值
                field.setAccessible(true);
                Object fieldValue = ReflectionUtils.getField(field, data);
                String keyName = StringUtils.join(shortName, "#", fieldValue);
                String msg = enumMsgHandler.getMsgByCode(keyName);
                if (StringUtils.isBlank(msg)) {
                    continue;
                }
                // 增加新属性
                Map<String, Object> map = new HashMap<>(1);
                map.put(field.getName() + "Name", msg);
                // 添加参数 field+Name 字段
                Object obj = DynamicBeanUtils.getObject(data, map);
                data = obj;
            }
        }
        return data;
    }
}