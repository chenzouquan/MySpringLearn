package dynamicProxy.cglib;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * cglib动态代理类
 * @author chenzou'quan
 */
public class CglibProxyExample implements MethodInterceptor {
    /**
     * 生成cglib动态代理对象
     *
     * @return Class类的cglib代理对象
     */
    public Object getProxy(Class cls){
        // CGLIB的增强类对象
        Enhancer enhancer = new Enhancer();
        // 设置增强类型
        enhancer.setSuperclass(cls);
        // 定义代理逻辑对象为当前对象，要求当前对象实现MethodInterceptor接口方法
        enhancer.setCallback(this);
        // 生成并返回代理对象
        return enhancer.create();
    }

    /**
     * 代理逻辑方法
     * @param proxy 代理对象
     * @param method 方法
     * @param args 方法参数
     * @param methodProxy 方法实现
     * @return 代理逻辑返回
     * @throws Throwable 异常
     */
    @Override
    public Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        System.out.println("调用真实方法前");
        // CGLIB反射调用真实对象方法
        Object result = methodProxy.invokeSuper(proxy, args);
        System.out.println("调用真实方法后");
        return result;
    }
}
