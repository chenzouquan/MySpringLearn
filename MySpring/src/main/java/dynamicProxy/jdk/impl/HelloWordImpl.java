package dynamicProxy.jdk.impl;

import dynamicProxy.jdk.HelloWord;

/**
 * 接口实现类
 * @author chenzou'quan
 */
public class HelloWordImpl implements HelloWord {
    @Override
    public void hello() {
        System.out.println("Hello Word!");
    }
}
