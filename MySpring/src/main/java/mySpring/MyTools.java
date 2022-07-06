package mySpring;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * IOC工具类
 * @author chenzou'quan
 */
public class MyTools {
    /**
     * 获取pack路径的包中的所有类，并存放到一个集合中
     * @param pack
     * @return
     */
    public static Set<Class<?>> getClasses(String pack) {
        // 1.建立一个class类集合
        Set<Class<?>> classes = new LinkedHashSet<Class<?>>();
        // 2. 获取包的名字并进行替换
        String packName = pack;
        // 3. 获取包目录
        String packDirName = packName.replace(".","/");
        // 4. 定义一个URL类，Class URL表示统一资源定位符，指向万维网上的“资源”的指针。
        URL dirs = Thread.currentThread().getContextClassLoader().getResource(packDirName);
        // 5. 获取URL对应文件名,并断言判断URL是否为空
        assert dirs != null;
        String firePath = dirs.getFile();
        // 6.获取文件夹
        File path = new File(firePath);
        // 7.判断 path 是否为文件夹
        boolean directory = path.isDirectory();
        if (directory){
            // 8. 获取文件里的所有文件
            File[] files = path.listFiles();
            for (File file: files) {
                // 9. 获取类的名称
                String className = file.getName().substring(0, file.getName().length() - 6);
                try {
                    // 10. 加载类
                    Class<?> aClass = Class.forName(pack + "." + className);
                    // 11. 放入集合中去
                    classes.add(aClass);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return classes;
    }
}
