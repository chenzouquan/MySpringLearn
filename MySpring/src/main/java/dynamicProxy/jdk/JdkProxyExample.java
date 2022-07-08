package dynamicProxy.jdk;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 代理对象
 * @author chenzou'quan
 */
public class JdkProxyExample implements InvocationHandler {

    /** 真实对象*/
    private Object target = null;

    /**
     *  建立代理对象和真实对象的代理关系，并返回代理对象
     * @param target 真实对象
     * @return 代理对象
     */
    public Object bind(Object target){
        this.target = target;
        /*
            第一个参数是类加载器
            第二个参数是把代理对象下挂到那些接口下
            第三个参数是定义实现方法逻辑的代理类，它必须实现InvocationHandler接口的invoke方法，它就是代理逻辑方法的实现方法。
         */
        return Proxy.newProxyInstance(target.getClass().getClassLoader(), target.getClass().getInterfaces(),this);
    }

    /**
     * 代理方法逻辑
     * @param proxy 代理对象
     *
     * @param method 当前调度方法
     *
     * @param args 当前方法参数
     *
     * @return 代理结果返回
     * @throws Throwable 异常
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        System.out.println("进入代理方法前");
        // 相当于调用目标方法
        Object invoke = method.invoke(target, args);
        System.out.println("代理方法后");
        return invoke;
    }
}
