package com.app.repository;

import com.app.ioc.annotation.Benchmark;

public class RepoBean implements RepoBeanInterface {
    public RepoBean() {
        System.out.println("RepoBean was created");
    }

    public void init() {
        System.out.println("RepoBean init was called");
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
