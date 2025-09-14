package com.hasan.lms_azure_demo.entity;

public class Employee {
    private int id;
    private String name;
    private double salary;

    // Constructors
    public Employee(int id, String name, double salary) {
        this.id = id;
        this.name = name;
        this.salary = salary;
    }

    // Getters and Setters
    public int getId() { return id; }
    public String getName() { return name; }
    public double getSalary() { return salary; }
    public void setSalary(double salary) { this.salary = salary; }
}
