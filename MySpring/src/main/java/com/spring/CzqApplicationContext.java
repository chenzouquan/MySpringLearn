package com.spring;




import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 自定义spring容器类
 * @author chenzou'quan
 */
public class CzqApplicationContext {

    /**
     * 配置类
     */
    private Class<?> configClass;
    /**
     * 单例池
     */
    private ConcurrentHashMap<String,Object> singletonObjects = new ConcurrentHashMap<>();

    private ConcurrentHashMap<String,BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();

    private List<BeanPostProcessor> beanPostProcessors = new ArrayList<>();

    public CzqApplicationContext(Class<?> configClass) {
        this.configClass = configClass;
        // 解析配置类
        // ComponentScan注解--》扫描路径--》扫描
        scan(configClass);
        // Beandefinition-->BeanDefinitionMap
        for (Map.Entry<String,BeanDefinition> entry: beanDefinitionMap.entrySet()) {
            String beanName = entry.getKey();
            BeanDefinition beanDefinition = entry.getValue();
            if (beanDefinition.getScope().equals("singleton")){
                // 单例Bean
                Object bean = createBean(beanName,beanDefinition);
                singletonObjects.put(beanName,bean);
            }
        }
    }

    /**
     * 创建bean方法
     * @param beanDefinition
     * @return
     */
    public Object createBean(String beanName,BeanDefinition beanDefinition){
        Class aClass = beanDefinition.getBeanClass();
        try {
            Object instance = aClass.getDeclaredConstructor().newInstance();
            
            // 对属性进行赋值（依赖注入）
            Field[] declaredFields = aClass.getDeclaredFields();
            for (Field field: declaredFields) {
                if (field.isAnnotationPresent(Autowire.class)){
                    // 获取容器对应bean
                    Object bean = getBean(field.getName());
                    if (bean == null){
                        throw new NullPointerException();
                    }
                    // 赋值
                    // setAccessible:将此对象的accessible标志设置为指示的布尔值。
                    // true的值表示反射对象应该在使用时抑制Java语言访问检查。
                    // false的值表示反映的对象应该强制执行Java语言访问检查。
                    field.setAccessible(true);
                    field.set(instance,bean);
                }
            }

            if (instance instanceof  BeanNameAware){
                ((BeanNameAware)instance).setBeanName(beanName);
            }

            // BeanPostProcessor bean扩展处理
            for (BeanPostProcessor beanPostProcessor : beanPostProcessors) {
                instance = beanPostProcessor.postProcessBeforeInitialization(instance, beanName);

            }

            // 初始化
            if (instance instanceof InitializingBean){
                ((InitializingBean)instance).afterPropertiesSet();
            }

            // BeanPostProcessor bean扩展处理
            for (BeanPostProcessor beanPostProcessor : beanPostProcessors) {
                instance = beanPostProcessor.postProcessAfterInitialization(instance, beanName);

            }
            return instance;
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 扫描方法 ComponentScan注解--》扫描路径--》扫描
     * @param configClass
     */
    private void scan(Class<?> configClass) {
        ComponentScan componentScanAnnotation = configClass.getDeclaredAnnotation(ComponentScan.class);
        if(componentScanAnnotation == null){
            throw new NullPointerException();
        }
        // 获取路径
        String path = componentScanAnnotation.value();
        // 扫描路径
        // 获取类加载器
        ClassLoader classLoader = this.getClass().getClassLoader();
        // 获取路径
        URL resource = classLoader.getResource(path.replace(".","/"));
        // 获取资源
        File file = new File(resource.getFile());
        // 判断文件是否为目录
        if (file.isDirectory()){
            File[] files = file.listFiles();
            for (File f: files) {
                // 获取类的全限定包名
                String fileName = f.getAbsolutePath();
                String className = fileName.substring(fileName.indexOf("com"),fileName.indexOf(".class"));
                className = className.replace("\\", ".");
                try {
                    // 加载类
                    Class<?> aClass = classLoader.loadClass(className);
                    // isAnnotationPresent(Class<? extends Annotation> annotationClass)
                    // 如果此元素上 存在指定类型的注释，则返回true，否则返回false。
                    if (aClass.isAnnotationPresent(Component.class)){
                        // 表示当前类是一个对象
                        // 解析类
                        // 判断这个类是否实现了BeanPostProcessor接口
                        if (BeanPostProcessor.class.isAssignableFrom(aClass)) {
                            BeanPostProcessor instance = (BeanPostProcessor) aClass.getDeclaredConstructor().newInstance();
                            beanPostProcessors.add(instance);
                        }

                        Component componentAnnotation = aClass.getDeclaredAnnotation(Component.class);
                        String beanName = componentAnnotation.value();
                        // 创建新的BeanDefinition定义类
                        BeanDefinition beanDefinition = new BeanDefinition();
                        beanDefinition.setBeanClass(aClass);
                        // 判断当前bean是单例bean还是prototype（原型bean）的bean
                        if (aClass.isAnnotationPresent(Scope.class)){
                            Scope scopeAnnotation = aClass.getDeclaredAnnotation(Scope.class);
                            beanDefinition.setScope(scopeAnnotation.value());
                        }else{
                            beanDefinition.setScope("singleton");
                        }
                        beanDefinitionMap.put(beanName,beanDefinition);
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    throw new RuntimeException(e);
                } catch (InstantiationException e) {
                    throw new RuntimeException(e);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                } catch (NoSuchMethodException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    /**
     * 获取bean组件方法
     * @param beanName
     * @return
     */
    public Object getBean(String beanName){
        if (beanDefinitionMap.containsKey(beanName)){
            BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
            if (beanDefinition.getScope().equals("singleton")){
                return singletonObjects.get(beanName);
            }else {
                // 如果是原型创建bean方法
                Object bean = createBean(beanName,beanDefinition);
                return bean;
            }
        }else{
            // 不存在对应的bean
            throw new NullPointerException();
        }
    }
}

