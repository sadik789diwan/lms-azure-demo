package com.hasan.lms_azure_demo.service;

@FunctionalInterface
public interface SalaryProcessor {
    double process(double currentSalary);
}
