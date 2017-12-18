package com.app.ioc.beandefinition;

public class SimpleBeanDefinition implements BeanDefinition {
    private final String beanName;
    private final Class<?> beanClass;

    public SimpleBeanDefinition(String beanName, Class<?> beanClass) {
        this.beanName = beanName;
        this.beanClass = beanClass;
    }

    @Override
    public String getBeanName() {
        return beanName;
    }

    @Override
    public Class<?> getBeanClass() {
        return beanClass;
    }
}
