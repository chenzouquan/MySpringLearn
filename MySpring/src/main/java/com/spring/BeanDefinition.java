package com.spring;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author chenzou'quan
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BeanDefinition {
    private String beanName;
    private Class beanClass;
    private String scope;
}
