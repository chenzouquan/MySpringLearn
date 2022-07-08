package dynamicProxy.cglib;

import dynamicProxy.jdk.HelloWord;
import dynamicProxy.jdk.impl.HelloWordImpl;

/**
 * @author chenzou'quan
 */
public class TestCglibProxy {
    public static void main(String[] args) {
        CglibProxyExample cglibProxyExample = new CglibProxyExample();
        HelloWordImpl proxy = (HelloWordImpl) cglibProxyExample.getProxy(HelloWordImpl.class);
        proxy.hello();
    }
}
