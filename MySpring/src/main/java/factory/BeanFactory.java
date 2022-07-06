package factory;

import dao.HelloDao;
import service.HelloService;
import service.impl.HelloServiceImpl;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * 静态工厂类
 * @author chenzou'quan
 */
public class BeanFactory {

    /** 提供配置文件类 */
    private static Properties properties;
    /** 缓存 */
    private static Map<String,Object> cache = new HashMap<>();

    static {
        properties = new Properties();
        try {
            properties.load(BeanFactory.class.getClassLoader().getResourceAsStream("factory.properties"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
   /**
    * 返回对应需求业务实现类。
    * 但效率还是很低，如何改良？使用反射
    * @Param bean ,组件的名称
    * @return
    */
   public static Object getDao(String bean){
       // 先判断缓存中是否存在这个bean
        if(!cache.containsKey(bean)){
            //双重检测
            synchronized (BeanFactory.class){
                //将bean存入缓存中去
                // 获得全类名
                String value = properties.getProperty("helloDao");
                // 反射机制创建对象
                try {
                    Class cls = Class.forName(value);
                    cache.put(bean,  cls.getConstructor(null).newInstance(null));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
       return cache.get(bean);
   }
}
