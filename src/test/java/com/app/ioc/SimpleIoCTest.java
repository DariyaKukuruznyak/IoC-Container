package com.app.ioc;

import com.app.ioc.beandefinition.BeanDefinition;
import com.app.ioc.config.Config;
import com.app.ioc.config.JavaConfig;
import com.app.repository.RepoBean;
import com.app.repository.RepoBeanInterface;
import com.app.repository.RepoBeanWithParam;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class SimpleIoCTest {

    private Config config = new Config() {
        @Override
        public List<String> beanNames() {
            return Collections.emptyList();
        }

        @Override
        public BeanDefinition beanDefinition(String beanName) {
            return null;
        }
    };

    @Test
    public void createIoC() {
        new SimpleIoC(config);
    }

    @Test
    public void beanDefShouldBeEmpty() {
        SimpleIoC simpleIoC = new SimpleIoC(config);
        List<String> beanDefinitions = simpleIoC.beanDefinitions();
        assertEquals(Collections.emptyList(), beanDefinitions);
    }

    @Test
    public void beanDefWithOneBeanInConfig() {
        final String beanName = "bean1";

        Config config = new Config() {
            @Override
            public List<String> beanNames() {
                return Arrays.asList(beanName);
            }

            @Override
            public BeanDefinition beanDefinition(String beanName) {
                return null;
            }
        };

        SimpleIoC simpleIoC = new SimpleIoC(config);
        List<String> beanDefinitions = simpleIoC.beanDefinitions();

        assertEquals(Arrays.asList(beanName), beanDefinitions);
    }

    @Test
    public void beanDefWithSeveralBeansInConfig() {
        final String beanName1 = "bean1";
        final String beanName2 = "bean2";

        Config config = new Config() {
            @Override
            public List<String> beanNames() {
                return Arrays.asList(beanName1, beanName2);
            }

            @Override
            public BeanDefinition beanDefinition(String beanName) {
                return null;
            }
        };

        SimpleIoC simpleIoC = new SimpleIoC(config);
        List<String> beanDefinitions = simpleIoC.beanDefinitions();

        assertEquals(Arrays.asList(beanName1, beanName2), beanDefinitions);
    }

    @Test(expected = IllegalArgumentException.class)
    public void beanNamesInConfigShouldBeUnique() {
        final String beanName1 = "bean1";
        final String beanName2 = "bean1";

        Config config = new Config() {
            @Override
            public List<String> beanNames() {
                if (beanName1.equals(beanName2)) {
                    throw new IllegalArgumentException();
                }
                return Arrays.asList(beanName1, beanName2);
            }

            @Override
            public BeanDefinition beanDefinition(String beanName) {
                return null;
            }
        };

        SimpleIoC simpleIoC = new SimpleIoC(config);
        simpleIoC.beanDefinitions();
    }

    @Test
    public void getBeanWithOneBeanInConfig() {

        final String testBeanName = "repoBean";
        final Class<?> testBeanClass = RepoBean.class;
        Map<String, Class<?>> beanDescriptions =
                new HashMap<String, Class<?>>() {{
                    put(testBeanName, testBeanClass);
                }};

        Config config = new JavaConfig(beanDescriptions);
        RepoBean testBean = null;
        SimpleIoC simpleIoC = new SimpleIoC(config);
        try {
            testBean = (RepoBean) simpleIoC.getBean(testBeanName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assertNotNull(testBean);
    }

    @Test
    public void beanShouldBeTheSame() throws Exception {

        final String beanName = "repoBean";
        final Class<?> beanClass = RepoBean.class;
        Map<String, Class<?>> beanDescriptions =
                new HashMap<String, Class<?>>() {{
                    put("repoBean", beanClass);

                }};

        Config config = new JavaConfig(beanDescriptions);
        SimpleIoC simpleIoC = new SimpleIoC(config);
        RepoBean testBean1 = (RepoBean) simpleIoC.getBean(beanName);
        RepoBean testBean2 = (RepoBean) simpleIoC.getBean(beanName);

        assertSame(testBean1, testBean2);
    }

    @Test
    public void getBeanWithDependencies() throws Exception {

        final String testBeanName = "repoBean";
        final Class<?> testBeanClass = RepoBean.class;

        final String dependentBeanName = "repoBeanWithParam";
        final Class<?> dependentBeanClass = RepoBeanWithParam.class;

        Map<String, Class<?>> beanDescriptions =
                new HashMap<String, Class<?>>() {{
                    put(testBeanName, testBeanClass);
                    put(dependentBeanName, dependentBeanClass);
                }};

        Config config = new JavaConfig(beanDescriptions);

        SimpleIoC simpleIoC = new SimpleIoC(config);
        RepoBeanWithParam testBeanWithDependency = (RepoBeanWithParam) simpleIoC.getBean(dependentBeanName);
        RepoBeanInterface testBean = testBeanWithDependency.getRepoBean();

        assertNotNull(testBean);
    }

}

