package com.app.repository;

import com.app.ioc.annotation.Benchmark;

public class RepoBeanWithParam implements RepoBeanInterface {
    private final RepoBean repoBean;

    public RepoBeanWithParam(RepoBean repoBean) {
        this.repoBean = repoBean;
        System.out.println("RepoBeanWithParam was created");
    }

    public void init() {
        System.out.println(repoBean.getClass().getSimpleName() + " init was called");
    }

    public RepoBeanInterface getRepoBean() {
        return repoBean;
    }

    @Override
    @Benchmark
    public double calculate() {
        double sum = 0;
        for (int i = 0; i < 100; i++) {
            double x = sum;
            sum += Math.pow(Math.sin(x), 2) + Math.pow(Math.cos(x), 2);
        }
        return sum;
    }
}
