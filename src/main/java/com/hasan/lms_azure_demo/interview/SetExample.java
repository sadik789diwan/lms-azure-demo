package com.hasan.lms_azure_demo.interview;

import java.util.*;

public class SetExample {
    public static void main(String[] args) {
        Set<String> hashSet = new HashSet<>();
        hashSet.add("Apple");
        hashSet.add("Banana");
        hashSet.add("Apple"); //map.put("A", PRESENT);
        //Since keys in a HashMap are unique, duplicates are automatically avoided.

        System.out.println("HashSet: " + hashSet);

        /**
         * Same as HashSet but maintains insertion order.
         * Internally backed by a LinkedHashMap.
         */
        Set<String> linkedHashSet = new LinkedHashSet<>();
        linkedHashSet.add("Apple");
        linkedHashSet.add("Banana");
        linkedHashSet.add("Apple");

        System.out.println("LinkedHashSet: " + linkedHashSet);

        Set<String> treeSet = new TreeSet<>();
      //  treeSet.add(null);  null not possible
        treeSet.add("Banana");
        treeSet.add("Apple");
        treeSet.add("Apple");
        treeSet.add("Banana"); //Uses a TreeMap (Red-Black Tree) internally.
        //Duplicates avoided because keys in a TreeMap must be unique.

        System.out.println("TreeSet: " + treeSet);
    }
}
