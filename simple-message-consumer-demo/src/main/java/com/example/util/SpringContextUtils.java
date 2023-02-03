package com.example.util;

import org.springframework.context.ApplicationContext;

public class SpringContextUtils {
    private static ApplicationContext ac;

    public static <T> T getBean (String beanName, Class<T> clazz) {
        T bean = ac.getBean(beanName,clazz);
        return bean;

    }
    public static <T> T getBean(Class<T> clazz) {
        T bean = ac.getBean(clazz);
        return bean;
    }

    public static void setApplicationContext(ApplicationContext applicationContext)
    {
        ac=applicationContext;

    }

}
