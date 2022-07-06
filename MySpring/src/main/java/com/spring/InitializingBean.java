package com.spring;

/**
 * @author chenzou'quan
 */
public interface InitializingBean {
    /**
     * 初始化方法
     * @throws Exception
     */
    void afterPropertiesSet() throws Exception;
}
