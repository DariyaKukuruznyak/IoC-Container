package com.app.ioc;

import com.app.ioc.config.Config;
import com.app.ioc.config.JavaConfig;
import com.app.repository.RepoBean;
import com.app.repository.RepoBeanInterface;
import com.app.repository.RepoBeanWithParam;

import java.util.HashMap;
import java.util.Map;

public class SimpleIoCRunner {
    public static void main(String[] args) {

        Map<String, Class<?>> beanDescriptions =
                new HashMap<String, Class<?>>() {{
                    put("repoBean", RepoBean.class);
                    put("repoBeanWithParam", RepoBeanWithParam.class);
                }};

        Config config = new JavaConfig(beanDescriptions);
        SimpleIoC ioC = new SimpleIoC(config);
        RepoBeanInterface repoBean = (RepoBeanInterface) ioC.getBean("repoBean");
        repoBean.calculate();
    }
}
