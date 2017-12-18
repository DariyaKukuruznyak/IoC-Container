package com.app.ioc.config;

import com.app.ioc.beandefinition.BeanDefinition;
import com.app.ioc.beandefinition.SimpleBeanDefinition;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JavaConfig implements Config {
    private final Map<String, Class<?>> beanDescriptions;

    public JavaConfig(Map<String, Class<?>> beanDescriptions) {
        this.beanDescriptions = beanDescriptions;
    }

    @Override
    public List<String> beanNames() {
        return new ArrayList<>(beanDescriptions.keySet());
    }

    @Override
    public BeanDefinition beanDefinition(String beanName) {
        Class<?> beanClass = beanDescriptions.get(beanName);
        return new SimpleBeanDefinition(beanName, beanClass);
    }
}
