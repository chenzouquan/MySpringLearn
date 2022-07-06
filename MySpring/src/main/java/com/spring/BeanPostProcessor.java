package com.spring;

/**
 * Bean的扩展处理器
 * @author chenzou'quan
 */
public interface BeanPostProcessor {
    /**
     * 在初始化前拓展操作
     * @param bean
     * @param beanName
     * @return
     */
    default Object postProcessBeforeInitialization(Object bean, String beanName) {
        return bean;
    }

    /**
     * 在初始化后拓展操作
     * @param bean
     * @param beanName
     * @return
     */
    default Object postProcessAfterInitialization(Object bean, String beanName){
        return bean;
    }

}
