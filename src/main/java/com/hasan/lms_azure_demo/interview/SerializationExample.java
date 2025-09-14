package com.hasan.lms_azure_demo.interview;

import java.io.*;

class Employee001 implements Serializable {
    private static final long serialVersionUID = 1L; // recommended
    private int id;
    private String name;

    public Employee001(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return id + " - " + name;
    }
}

public class SerializationExample {
    public static void main(String[] args) {
        Employee001 emp = new Employee001(101, "John");

        // Serialization (Object -> File)
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("C:\\SadikPersonalInfo\\GIT_CICD\\lms-azure-demo\\employee.ser"))) {
            oos.writeObject(emp);
            System.out.println("Employee Serialized!");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Deserialization (File -> Object)
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("C:\\SadikPersonalInfo\\GIT_CICD\\lms-azure-demo\\employee.ser"))) {
            Employee001 deserializedEmp = (Employee001) ois.readObject();
            System.out.println("Deserialized Employee: " + deserializedEmp);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
