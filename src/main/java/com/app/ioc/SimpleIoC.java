package com.app.ioc;

import com.app.ioc.annotation.Benchmark;
import com.app.ioc.beandefinition.BeanDefinition;
import com.app.ioc.config.Config;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimpleIoC {
    private Config config;
    private Map<String, Object> beanMap;

    public SimpleIoC(Config config) {
        this.config = config;
        beanMap = new HashMap<>();
    }

    public List<String> beanDefinitions() {
        return config.beanNames();
    }

    public Object getBean(String beanName) {
        if (beanMap.containsKey(beanName)) {
            return beanMap.get(beanName);
        } else {
            Object bean = getBeanFromConfig(beanName);
            beanMap.put(beanName, bean);
            return bean;
        }
    }

    private Object getBeanFromConfig(String beanName) {
        BeanDefinition beanDefinition = config.beanDefinition(beanName);
        Class<?> classOfBean = beanDefinition.getBeanClass();
        Constructor<?>[] constructors = classOfBean.getConstructors();

        if (constructors.length != 1) {
            throw new IllegalStateException("Invalid number of constructors");
        }

        Constructor<?> constructor = constructors[0];
        Parameter[] parameters = constructor.getParameters();
        Object instanceOfBean;
        if (parameters.length == 0) {
            instanceOfBean = callConstructorWithoutParameters(classOfBean);
        } else {
            Object[] parameterBeans = injectDependencies(parameters);
            instanceOfBean = callConstructorWithParameters(classOfBean, parameterBeans);
        }

        callInitMethod(instanceOfBean);

        return injectAnnotations(instanceOfBean);
    }

    private Object[] injectDependencies(Parameter[] parameters) {
        Object[] parameterBeans = new Object[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            String parameterName = parameters[i].getType().getSimpleName();
            String processedName = parameterName.substring(0, 1).toLowerCase()
                    + parameterName.substring(1);
            Object parameterBean = getBean(processedName);
            parameterBeans[i] = parameterBean;
        }
        return parameterBeans;
    }

    private Object injectAnnotations(Object bean) {
        for (Method method : bean.getClass().getMethods()) {
            Annotation[] annotations = method.getAnnotations();
            for (Annotation annotation : annotations) {
                if (annotation instanceof Benchmark) {
                    bean = createBenchMarkProxy(bean);
                }
            }
        }
        return bean;
    }

    private Object createBenchMarkProxy(Object bean) {
        return Proxy.newProxyInstance(
                ClassLoader.getSystemClassLoader(),
                bean.getClass().getInterfaces(),
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        if (method.isAnnotationPresent(Benchmark.class)) {
                            return calculateInvokingTime(method, bean);
                        }
                        return method.invoke(bean);
                    }
                }
        );
    }

    private Object calculateInvokingTime(Method method, Object bean) throws InvocationTargetException, IllegalAccessException {
        Long startTime = System.nanoTime();
        Object invoke = method.invoke(bean);
        Long endTime = System.nanoTime();
        Long interval = (endTime - startTime) / 1000;
        System.out.println("Method " + method.getName() +
                " has worked for " + interval + " sec");
        return invoke;
    }

    private Object callConstructorWithoutParameters(Class<?> classOfBean) {
        try {
            return classOfBean.getConstructors()[0].newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException("Error during constructor without parameters call");
        }
    }

    private Object callConstructorWithParameters(Class<?> classOfBean, Object[] parameterBeans) {
        try {
            return classOfBean.getConstructors()[0].newInstance(parameterBeans);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException("Error during constructor with parameters call");
        }
    }

    private void callInitMethod(Object bean) {
        Method[] methods = bean.getClass().getMethods();
        for (Method method : methods) {
            if (method.getName().equals("init")) {
                callMethodOnBean(bean, method);
                return;
            }
        }
    }

    private void callMethodOnBean(Object bean, Method method) {
        try {
            method.invoke(bean);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new IllegalStateException("Error during " + method.getName() + " call");
        }
    }
}
