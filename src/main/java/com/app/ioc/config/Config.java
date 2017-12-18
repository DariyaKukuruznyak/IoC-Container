package com.app.ioc.config;

import com.app.ioc.beandefinition.BeanDefinition;

import java.util.List;

public interface Config {

    List<String> beanNames();

    BeanDefinition beanDefinition(String beanName);
}