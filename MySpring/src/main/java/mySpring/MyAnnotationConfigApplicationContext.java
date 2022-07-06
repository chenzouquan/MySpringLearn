package mySpring;


import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 *
 * @author chenzou'quan
 */
public class MyAnnotationConfigApplicationContext {
    /**
     * IOC容器
     */
    private Map<String,Object> Ioc = new HashMap<>();
    public MyAnnotationConfigApplicationContext(String pack) {
        // 遍历包，找到目标类(原材料)
        Set<BeanDefinition> beanDefinitions = findBeanDefinitions(pack);
        // 根据原材料创建bean
        createObject(beanDefinitions);
        // 自动装载
        autowireObject(beanDefinitions);
    }

    public void autowireObject(Set<BeanDefinition> beanDefinitions){
        Iterator<BeanDefinition> iterator = beanDefinitions.iterator();
        while (iterator.hasNext()){
            BeanDefinition next = iterator.next();
            Class beanClass = next.getBeanClass();
            // 返回的数组 Field对象反映此表示的类或接口声明的所有字段 类对象。
            Field[] fields = beanClass.getDeclaredFields();
            for (Field field: fields) {
                Autowire annotation = field.getAnnotation(Autowire.class);
                if (annotation != null){
                    Qualifier qualifier = field.getAnnotation(Qualifier.class);
                    if (qualifier != null){
                        // byName
                        try {
                            String beanName = qualifier.value();
                            Object bean = getBean(beanName);
                            String fieldName = field.getName();
                            String methodName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                            Method method = beanClass.getMethod(methodName, field.getType());
                            Object o = getBean(next.getBeanName());
                            method.invoke(o,bean);
                        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                            throw new RuntimeException(e);
                        }
                    }else{
                        // byType

                    }
                }
            }
        }
    }

    /**
     * 获取组件方法
     * @param beanName
     * @return
     */
    public Object getBean(String beanName){
        return Ioc.get(beanName);
    }

    /**
     * 创建Bean组件方法
     * @param beanDefinitions
     */
    public void createObject(Set<BeanDefinition> beanDefinitions){
        Iterator<BeanDefinition> iterator = beanDefinitions.iterator();
        while (iterator.hasNext()){
            BeanDefinition next = iterator.next();
            Class beanClass = next.getBeanClass();
            String beanName = next.getBeanName();
            try {
                // 创建的对象
                Object o = beanClass.getConstructor().newInstance();
                // 完成属性的赋值
                // 返回的数组 Field对象反映此表示的类或接口声明的所有字段 类对象。
                Field[] declaredFields = beanClass.getDeclaredFields();
                for (Field field: declaredFields) {
                    Value annotation = field.getAnnotation(Value.class);
                    if (annotation != null){
                        String value = annotation.value();
                        // 通过set方法设置
                        String name = field.getName();
                        String methodName = "set" + name.substring(0, 1).toUpperCase() + name.substring(1);
                        Method method = beanClass.getMethod(methodName,field.getType());
                        // 完成数据类型转换
                        Object val = null;
                        switch (field.getType().getName()){
                            case "java.lang.Integer":
                                val = Integer.parseInt(value);
                                break;
                            case "java.lang.String":
                                val = value;
                                break;
                            case "java.lang.Float":
                                val = Float.parseFloat(value);
                                break;
                            default:
                                break;
                        }
                        method.invoke(o,val);

                    }
                }
                // 存放到IOC容器中
                Ioc.put(beanName,o);
            } catch (InstantiationException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * 获取Bean原材料方法
     * @param pack
     * @return
     */
    public Set<BeanDefinition> findBeanDefinitions(String pack){
        Set<BeanDefinition> beanDefinitions = new LinkedHashSet<>();
        // 1.获取包下所有的类
        Set<Class<?>> classes = MyTools.getClasses(pack);
        // 2.遍历这些类，找到添加了注解的类
        for (Class<?> cls : classes) {
            Component annotation = cls.getAnnotation(Component.class);
            if (annotation != null){
                // 3.将这些类封装成BeanDefinition，装载到集合中
                // 3.1 获取Component注解的值(Value)
                String beanName = annotation.value();
                if (beanName.isEmpty()){
                    // 3.2 类名首字母小写
                    String packageName = cls.getPackage().getName()+".";
                    String className = cls.getName().replaceAll(packageName, "");
                    beanName = className.substring(0, 1).toLowerCase() + className.substring(1);
                }
                // 3.3 封装到集合中
                BeanDefinition beanDefinition = new BeanDefinition(beanName, cls);
                beanDefinitions.add(beanDefinition);
            }
        }
        return beanDefinitions;
    }

    public static void main(String[] args) {
        MyAnnotationConfigApplicationContext myAnnotationConfigApplicationContext = new MyAnnotationConfigApplicationContext("mySpring.entity");
        System.out.println(myAnnotationConfigApplicationContext.getBean("account"));
    }
}
