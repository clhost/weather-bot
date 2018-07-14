package com.clhost.weatherbot.logger;

import org.apache.logging.log4j.LogManager;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.lang.Nullable;

import java.lang.reflect.Field;

public class LoggingAnnotationProcessor implements BeanPostProcessor {
    @Nullable
    @Override
    public Object postProcessBeforeInitialization(@NotNull Object bean, String beanName) throws BeansException {
        Class clazz = bean.getClass();
        do {
            for (Field field : clazz.getDeclaredFields()) {
                Logging annotation = field.getAnnotation(Logging.class);
                if (annotation != null) {
                    boolean accessible = field.isAccessible();
                    field.setAccessible(true);
                    try {
                        field.set(bean, LogManager.getLogger(clazz));
                    } catch (IllegalAccessException e) {
                        LogManager.getLogger(this.getClass()).error(e);
                    }
                    field.setAccessible(accessible);
                }
            }
            clazz = clazz.getSuperclass();
        } while (clazz != null);

        return bean;
    }
}
