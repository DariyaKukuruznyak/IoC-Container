package com.app.repository;

import com.app.ioc.annotation.Benchmark;

public interface RepoBeanInterface {
    @Benchmark
    double calculate();
}
