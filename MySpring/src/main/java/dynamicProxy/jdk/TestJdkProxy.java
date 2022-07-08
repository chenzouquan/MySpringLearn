package dynamicProxy.jdk;

import dynamicProxy.jdk.impl.HelloWordImpl;

/**
 * 测试JDK动态代理
 * @author chenzou'quan
 */
public class TestJdkProxy {
    public static void main(String[] args) {
        JdkProxyExample jdkProxyExample = new JdkProxyExample();
        // 绑定关系
        HelloWord proxy = (HelloWord)jdkProxyExample.bind(new HelloWordImpl());
        // 因为proxy是代理对象，所以会进入代理的逻辑方法invoke里
        proxy.hello();
    }
}
