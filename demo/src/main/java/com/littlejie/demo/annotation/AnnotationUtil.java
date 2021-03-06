package com.littlejie.demo.annotation;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * 处理使用注解的类，代码来自互联网，尚未理解
 * Created by littlejie on 2017/1/18.
 */
public class AnnotationUtil {


    public static List<String> getDescriptions(List<Class<?>> classes) {
        List<String> lstDescription = new ArrayList<>();
        for (Class<?> clazz : classes) {
            lstDescription.add(getDescription(clazz));
        }
        return lstDescription;
    }

    /**
     * 获取 Class 的描述
     *
     * @param clazz
     * @return 如果没有使用 Description 注解，则直接返回类名；否则，返回 description() 方法的值
     */
    public static String getDescription(Class<?> clazz) {
        Description description = clazz.getAnnotation(Description.class);
        if (description == null) {
            return clazz.getSimpleName();
        }
        return description.description();
    }

    public static List<String> getTitles(List<Class<?>> classes) {
        List<String> lstDescription = new ArrayList<>();
        for (Class<?> clazz : classes) {
            lstDescription.add(getDescription(clazz));
        }
        return lstDescription;
    }

    /**
     * 获取 Class 的 Title 描述
     *
     * @param clazz
     * @return 如果没有使用 Title 注解，则直接返回类名；否则，返回 title() 方法的值
     */
    public static String getTitle(Class<?> clazz) {
        Title title = clazz.getAnnotation(Title.class);
        if (title == null) {
            return clazz.getSimpleName();
        }
        return title.title();
    }

    /*-------------以下代码来自网络--------------*/

    /**
     * 查找所有使用注解的类
     *
     * @param annotationClass
     * @return
     */
    @SuppressWarnings("unchecked")
    public static List<Class> getAllClassByAnnotation(Class annotationClass) {
        List returnClassList = new ArrayList<Class>();
        //判断是不是注解
        if (annotationClass.isAnnotation()) {
            String packageName = annotationClass.getPackage().getName();    //获得当前包名
            try {
                List<Class> allClass = getClasses(packageName);//获得当前包以及子包下的所有类

                for (int i = 0; i < allClass.size(); i++) {
                    if (allClass.get(i).isAnnotationPresent(annotationClass)) {
                        returnClassList.add(allClass.get(i));
                    }
                }
            } catch (Exception e) {
                // TODO: handle exception
            }
        }
        return returnClassList;
    }

    /**
     * @param packageName 包名
     * @return List<Class>    包下所有类
     * @Description: 根据包名获得该包以及子包下的所有类不查找jar包中的
     */
    private static List<Class> getClasses(String packageName) throws ClassNotFoundException, IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        String path = packageName.replace(".", "/");
        Enumeration<URL> resources = classLoader.getResources(path);
        List<File> dirs = new ArrayList<File>();
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            String newPath = resource.getFile().replace("%20", " ");
            dirs.add(new File(newPath));
        }
        ArrayList<Class> classes = new ArrayList<Class>();
        for (File directory : dirs) {
            classes.addAll(findClass(directory, packageName));
        }
        return classes;
    }

    private static List<Class> findClass(File directory, String packageName)
            throws ClassNotFoundException {
        List<Class> classes = new ArrayList<Class>();
        if (!directory.exists()) {
            return classes;
        }
        File[] files = directory.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                assert !file.getName().contains(".");
                classes.addAll(findClass(file, packageName + "." + file.getName()));
            } else if (file.getName().endsWith(".class")) {
                classes.add(Class.forName(packageName + "." + file.getName().substring(0, file.getName().length() - 6)));
            }
        }
        return classes;
    }

}
