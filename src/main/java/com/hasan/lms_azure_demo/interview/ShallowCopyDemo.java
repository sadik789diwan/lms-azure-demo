package com.hasan.lms_azure_demo.interview;

class Address {
    String city;

    public Address(String city) {
        this.city = city;
    }
    @Override
    protected Object clone() throws CloneNotSupportedException {
        return new Address(this.city); // clone Address explicitly
    }
}

class Employee implements Cloneable {
    int id;
    String name;
    Address address;

    public Employee(int id, String name, Address address) {
        this.id = id;
        this.name = name;
        this.address = address;
    }

    // Shallow copy using clone()
    @Override
    protected Object clone() throws CloneNotSupportedException {
       Employee cloned = (Employee) super.clone();
        cloned.address = (Address) address.clone(); // deep copy nested object
        return cloned;
  /*      return super.clone();  // default shallow copy*/
    }
}

/**
 * In Java, a shallow copy copies fields as they are (primitive values and object references), so nested objects are shared.
 * A deep copy not only copies the main object but also creates new copies of nested objects, ensuring full independence.
 * Shallow copy can be done via super.clone(), whereas deep copy requires overriding clone(), using a copy constructor, or using serialization.
 */
public class ShallowCopyDemo {
    public static void main(String[] args) throws CloneNotSupportedException {
        Address address = new Address("Pune");
        Employee emp1 = new Employee(1, "John", address);

        Employee emp2 = (Employee) emp1.clone();  // shallow copy

        emp2.address.city = "Mumbai";  // changing nested object

        System.out.println(emp1.address.city); // Output: Mumbai 😮
        System.out.println(emp2.address.city); // Output: Mumbai

    }
}
