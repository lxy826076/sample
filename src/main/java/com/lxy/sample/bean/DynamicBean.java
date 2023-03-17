package com.lxy.sample.bean;



import com.lxy.sample.interfaces.GlobalEnum;
import org.springframework.cglib.beans.BeanGenerator;
import org.springframework.cglib.beans.BeanMap;

import java.util.Map;

/**
 * <p>
 *  动态bean复制
 * </p>
 *
 * @author xiaoyi.liu1
 * @since 2023/3/14 下午2:15
 */
public class DynamicBean {

    public static void main(String[] args) {
        System.out.println("GlobalEnum.class.getPackage() = " + GlobalEnum.class.getName());
    }

    private Object target;
    private BeanMap beanMap;

    public DynamicBean(Class superclass, Map<String, Class> propertyMap) {
        this.target = generateBean(superclass, propertyMap);
        this.beanMap = BeanMap.create(this.target);
    }

    public void setValue(String property, Object value) {
        beanMap.put(property, value);
    }

    public Object getValue(String property) {
        return beanMap.get(property);
    }

    public Object getTarget() {
        return this.target;
    }
    /**
     * 根据属性生成对象
     *
     */
    private Object generateBean(Class superclass, Map<String, Class> propertyMap) {
        BeanGenerator generator = new BeanGenerator();
        if (null != superclass) {
            generator.setSuperclass(superclass);
        }
        BeanGenerator.addProperties(generator, propertyMap);
        return generator.create();
    }
}

