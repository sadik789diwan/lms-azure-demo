package com.hasan.lms_azure_demo.controller;

import com.hasan.lms_azure_demo.entity.Employee;
import com.hasan.lms_azure_demo.service.EmployeeService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService service;

    public EmployeeController(EmployeeService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public Employee getEmployee(@PathVariable int id) {
        return service.getEmployeeById(id);
    }

    // Example: Increment Salary by 10%
    @PutMapping("/{id}/increment")
    public Employee incrementSalary(@PathVariable int id) {
        return service.updateSalary(id, salary -> salary * 1.10);
    }

    // Example: Deduct Tax 5000
    @PutMapping("/{id}/deduct")
    public Employee deductTax(@PathVariable int id) {
        return service.updateSalary(id, salary -> salary - 5000);
    }
}
