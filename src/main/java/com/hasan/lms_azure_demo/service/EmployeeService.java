package com.hasan.lms_azure_demo.service;

import com.hasan.lms_azure_demo.entity.Employee;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class EmployeeService {
    private final Map<Integer, Employee> employeeDB = new HashMap<>();

    public EmployeeService() {
        employeeDB.put(1, new Employee(1, "John", 50000));
        employeeDB.put(2, new Employee(2, "Alice", 60000));
    }

    public Employee getEmployeeById(int id) {
        return employeeDB.get(id);
    }

    public Employee updateSalary(int id, SalaryProcessor processor) {
        Employee emp = employeeDB.get(id);
        if (emp != null) {
            double newSalary = processor.process(emp.getSalary());
            emp.setSalary(newSalary);
        }
        return emp;
    }
}
