package com.hasan.lms_azure_demo.interview;

import java.util.ArrayList;
import java.util.List;

public class MiddleElementFinder {

    public static <T> T findMiddle(List<T> list) {
        // Step 1: Count total elements (without using size())
        int count = 0;
        for (T element : list) {
            count++;
        }

        // Step 2: Find middle index
        int middleIndex = count / 2; // if odd -> middle, if even -> right-middle

        // Step 3: Traverse again to find middle element
        int currentIndex = 0;
        for (T element : list) {
            if (currentIndex == middleIndex) {
                return element;
            }
            currentIndex++;
        }
        return null; // if list is empty
    }

    public static void main(String[] args) {
        List<Integer> numbers = new ArrayList<>();
        numbers.add(10);
        numbers.add(20);
        numbers.add(30);
        numbers.add(40);
        numbers.add(50);

        Integer middle = findMiddle(numbers);
        System.out.println("Middle element: " + middle);
    }
}
