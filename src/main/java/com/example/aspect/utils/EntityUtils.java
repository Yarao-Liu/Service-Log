package com.example.aspect.utils;

import com.example.aspect.po.SysRecord;
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
 * @vesion 1.0
 * @date 2020/10/19
 * @since jdk1.8
 */
@Slf4j
public class EntityUtils {
    private EntityUtils() {
    }

    /**
     * 获取某个包下的所有类
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
     */
    private static Class<?> loadClass(String className) {
        return loadClass(className, true);
    }

    private static void doAddClass(Set<Class<?>> classSet, String className) {
        Class<?> cls = loadClass(className, false);
        classSet.add(cls);
    }

    /**
     * assertClass 判断是否为自定义类，是返回类名，否返回null
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
     * 将object还原
     * @param args
     * @param packageName
     * @return
     */
    public static ArrayList<Object> reBuildClass(Object[] args, String packageName) {
        ArrayList<Object> objects = new ArrayList<>();
        Set packageAllClasses = EntityUtils.getPackageAllClasses(packageName);
        Iterator iterator = packageAllClasses.iterator();
        //可以组装 describe 和 details
        for (int i = 0; i < args.length; i++) {
            log.error("2: " + args[i]);
            log.error("3: " + args[i].getClass());
            //param的参数类型
            while (iterator.hasNext()){
                Object next = iterator.next();
                if (args[i].getClass().equals(next)){
                    Object o = args[i].getClass().cast(args[i]);
                    objects.add(o);
                }
            }
        }
        return objects;
    }
    /**
     * 调用示例
     */
    public static void main(String[] args) {
        SysRecord sysRecord = new SysRecord();
        Class aClass = assertClass(sysRecord, "com.example.aspect.po");
        System.out.println(aClass);
    }
}
