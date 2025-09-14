package com.hasan.lms_azure_demo.interview;

import java.lang.reflect.Field;
import java.util.Date;

/**
 * To create an immutable class:
 * Mark the class as final.
 * Make all fields private and final.
 * Initialize fields only via constructor.
 * Don’t provide setters.
 * For mutable objects, perform a deep copy in both constructor and getter.
 */
final class ImmutableEmployee{
    private final String name;
    private final int id;
    private final Date date;
    ImmutableEmployee(String name, int id, Date date){
        this.name=name;
        this.id= id;
        this.date= new Date(date.getTime());
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Date getDate() {
        return new Date(date.getTime());
    }
}
public class TestImmutable {
    public static void main(String[] args) throws Exception {
        Date date=new Date();
        ImmutableEmployee emp= new ImmutableEmployee("Sadik",10, date);
        System.out.println(emp.getDate());  // still original ✅
        date.setTime(0);
        System.out.println(emp.getDate());  // still original ✅

        ImmutableEmployee emp1 = new ImmutableEmployee("John",10, new Date());

        Field field = ImmutableEmployee.class.getDeclaredField("name");
        field.setAccessible(true);
        field.set(emp1, "Hacked");

        System.out.println(emp1.getName()); // Prints: Hacked

       /* String name ="hello";
        Field valueField =String.class.getDeclaredField("value");
        valueField.set(name, "World".toCharArray());
        System.out.println(name);*/
    }
}
