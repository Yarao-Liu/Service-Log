package com.example.aspect.utils;

import com.example.aspect.po.BaseRecord;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import static cn.hutool.core.util.ClassUtil.getClassLoader;

/**
 * //TODO
 *
 * @author liuyarao
 * @since jdk1.8
 */
@Slf4j
public class EntityUtils {
    private EntityUtils() {
    }

    /**
     * 获取包下所有类的全类名
     *
     * @param packageName 要获取所有类的包的包名
     * @return Set
     * @throws IOException IO异常
     */
    public static Set<Class<?>> getClasses(String packageName) throws IOException {
        Set<Class<?>> classSet = new HashSet<>();
        /**获取资源路径*/
        Enumeration<URL> urls = Thread.currentThread().getContextClassLoader().getResources(packageName.replace(".", "/"));

        while (urls.hasMoreElements()) {

            URL url = urls.nextElement();

            if (url != null) {
                String protocol = url.getProtocol();

                if ("file".equals(protocol)) {
                    String packagePath = url.getPath().replaceAll("%20", " ");
                    addClass(classSet, packagePath, packageName);
                } else if ("jar".equals(protocol)) {
                    JarURLConnection jarURLConnection = (JarURLConnection) url.openConnection();
                    if (jarURLConnection != null) {
                        JarFile jarFile = jarURLConnection.getJarFile();
                        if (jarFile != null) {
                            Enumeration<JarEntry> jarEntries = jarFile.entries();
                            while (jarEntries.hasMoreElements()) {
                                JarEntry jarEntry = jarEntries.nextElement();
                                String jarEntryName = jarEntry.getName();
                                if (jarEntryName.endsWith(".class")) {
                                    String className = jarEntryName.substring(0, jarEntryName.lastIndexOf(".")).replaceAll("/", ".");
                                    doAddClass(classSet, className);
                                }
                            }
                        }
                    }
                }
            }
        }
        return classSet;
    }

    /**
     * 扫描包名/包路径下所有类的全类名
     *
     * @param classSet    存储工具集
     * @param packagePath 包路径
     * @param packageName 包名称
     */
    private static void addClass(Set<Class<?>> classSet, String packagePath, String packageName) {
        File[] files = new File(packagePath).listFiles(file -> (file.isFile() && file.getName().endsWith(".class")) || file.isDirectory());
        if (files != null) {
            for (File file : files) {
                String fileName = file.getName();
                if (file.isFile()) {
                    String className = fileName.substring(0, fileName.lastIndexOf("."));
                    if (StringUtils.isNotEmpty(packageName)) {
                        className = packageName + "." + className;
                    }
                    doAddClass(classSet, className);
                } else {
                    String subPackagePath = fileName;
                    if (StringUtils.isNotEmpty(packagePath)) {
                        subPackagePath = packagePath + "/" + subPackagePath;
                    }
                    String subPackageName = fileName;
                    if (StringUtils.isNotEmpty(packageName)) {
                        subPackageName = packageName + "." + subPackageName;
                    }
                    addClass(classSet, subPackagePath, subPackageName);
                }
            }
        }
    }

    /**
     * 加载类
     *
     * @param className     加载类的类名
     * @param isInitialized 是否初始化类
     */
    private static Class<?> loadClass(String className, boolean isInitialized) {
        Class<?> cls;
        try {
            cls = Class.forName(className, isInitialized, getClassLoader());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return cls;
    }

    /**
     * 加载类（默认将初始化类）
     *
     * @param className 加载类的类名
     */
    private static Class<?> loadClass(String className) {
        return loadClass(className, true);
    }

    /**
     * 真正执行addClass操作
     *
     * @param classSet  存储所有的类信息
     * @param className 添加的全类名
     */
    private static void doAddClass(Set<Class<?>> classSet, String className) {
        Class<?> cls = loadClass(className, false);
        classSet.add(cls);
    }

    /**
     * 判断是否为自定义类，是返回类名，否返回null
     *
     * @param obj         传入的参数
     * @param packageName 扫描的包名
     * @return 返回obj的类型信息
     */
    public static Class assertClass(Object obj, String packageName) {
        try {
            HashSet<Class<?>> classes = (HashSet<Class<?>>) getClasses(packageName);
            Iterator<Class<?>> iterator = classes.iterator();
            while (iterator.hasNext()) {
                Class<?> next = iterator.next();
                Class aClass = obj.getClass();
                if (next.equals(aClass)) {
                    return aClass;
                }
            }
        } catch (IOException e) {
            return null;
        }
        return null;
    }

    /**
     * 异常处理,异常不返回给调用端
     *
     * @param packageName 扫描的包名
     * @return 包下所有的全类名
     */
    public static Set getPackageAllClasses(String packageName) {
        Set<Class<?>> classes;
        try {
            classes = getClasses(packageName);
        } catch (IOException ignored) {
            return null;
        }
        return classes;
    }

    /**
     * 将object还原为原来的Class
     *
     * @param args        请求的参数
     * @param packageName 加载的包名
     * @return ArrayList 全类名的list
     */
    public static ArrayList<Object> reBuildClass(Object[] args, String packageName) {
        ArrayList<Object> objects = new ArrayList<>();
        Set packageAllClasses = EntityUtils.getPackageAllClasses(packageName);
        Iterator iterator = packageAllClasses.iterator();
        //可以组装 describe 和 details
        for (int i = 0; i < args.length; i++) {
            log.warn("rebuild: " + args[i]);
            log.warn("rebuild: " + args[i].getClass());
            //param的参数类型
            while (iterator.hasNext()) {
                Object next = iterator.next();
                if (args[i].getClass().equals(next)) {
                    Object o = args[i].getClass().cast(args[i]);
                    objects.add(o);
                }
            }
        }
        return objects;
    }

    public static boolean isPrimitive(Object obj) {
        if (obj instanceof String) {
            return true;
        }
            try {
                return ((Class<?>) obj.getClass().getField("TYPE").get(null)).isPrimitive();
            } catch (Exception e) {
                return false;
            }
    }
    public static String Obj2String(Object obj) {
        return String.valueOf(obj);
    }
    public static ArrayList<String> param2String(Object[] params){
        ArrayList<String> paramlist = new ArrayList<>();
        for (int i=0;i<params.length;i++){
            paramlist.add((String) params[i]);
        }
        return paramlist;
    }

    /**
     * @param args 无意义
     */
    public static void main(String[] args) {
        BaseRecord baseRecord = new BaseRecord();
        Class aClass = assertClass(baseRecord, "com.example.aspect.po");
        System.out.println(aClass);
        Double d = new Double(0.3);
        String a = new String("abc");
        boolean primitive = isPrimitive(a);
        System.out.println(primitive);
        int c=10;

        String s = Obj2String(d);
        System.out.println(s);

    }
}
