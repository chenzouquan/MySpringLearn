package com.czq.service;

import com.spring.BeanPostProcessor;
import com.spring.Component;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;


// 需要装载
@Component("czqBeanPostProcessor")
/** e
 * @author chenzou'quan
 */
public class CzqBeanPostProcessor implements BeanPostProcessor {
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        System.out.println("初始化前");
        if(beanName.equals("userService")){
            ((UserServiceImpl)bean).setBeanName("xixixi");
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) {
        System.out.println("初始化后");
        if (beanName.equals("userService")){
            // JDK动态代理对象,为什么动态代理必须针对接口？JDK动态代理的原理是根据定义好的规则，用传入的接口创建一个新类
            Object proxyInstance = Proxy.newProxyInstance(this.getClass().getClassLoader(), bean.getClass().getInterfaces(), new InvocationHandler() {
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    System.out.println("代理逻辑");
                    return method.invoke(bean,args);
                }
            });
            return proxyInstance;
        }
        return bean;
    }
}
