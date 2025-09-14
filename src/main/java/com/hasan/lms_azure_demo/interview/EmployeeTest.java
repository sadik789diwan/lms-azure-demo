package com.hasan.lms_azure_demo.interview;

import java.util.*;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class EmployeeTest {
    public static void main(String args[]) {
        List<Employee01> emp = List.of(
                new Employee01(1, "John", "pune", 50000.0, 30),
                new Employee01(3, "Rakesh", "Hyd", 50000.0, 40),
                new Employee01(4, "Rakesh", "Hyd", 50000.0, 40),
                new Employee01(5, "Pavan", "Mumbai", 50000.0, 70)
        );


/*     List<Employee01> updateEmp=   emp.stream().filter(i->i.getAge()> 28).map(p->new Employee01(p.getId(),p.getCity(),p.getName(),p.getSalary()+8000, p.getAge())).collect(Collectors.toList());
     System.out.println(updateEmp);*/

       List<Employee01> duplicate= emp.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet().stream().filter(e->e.getValue() > 1)
                .map(Map.Entry::getKey).collect(Collectors.toList());
        System.out.println(duplicate);

    }
}

class Employee01 {
    private int id;
    private String name;
    private String city;
    private Double salary;
    private int age;

    public Employee01(int id, String name, String city, Double salary, int age) {
        this.id = id;
        this.name = name;
        this.city = city;
        this.salary = salary;
        this.age = age;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Employee01{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", city='" + city + '\'' +
                ", salary=" + salary +
                ", age=" + age +
                '}';
    }
}
