package com.hasan.lms_azure_demo.interview;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class FlatMap2DArray {
    public static void main(String args[]){
        int[] [] num={{1,2,3}, {4,5,6}, {7,8,9}};
       List<Integer> result= Arrays.stream(num)
               .flatMapToInt(Arrays::stream).boxed().collect(Collectors.toList());
       System.out.println(result);

      Arrays.stream(num)
              .map(Arrays::stream)
             .toList().forEach(i->{
                 System.out.println(i.boxed().collect(Collectors.toList()));
              });

        Integer[][] arr = { {1,2,3}, {4,5,6}, {7,8,9} };
        // Convert 2D array → Stream<Integer>
        List<Integer> flatList = Arrays.stream(arr)       // Stream<Integer[]>
                .flatMap(Arrays::stream) // flatten to Stream<Integer>
                .collect(Collectors.toList());
        System.out.println(flatList);

        List<List<String>> data = List.of(List.of("a", "b"), List.of("c", "d"));
        List<String> flat = data.stream()
                .flatMap(List::stream)
                .toList();
        System.out.println(flat); // [a, b, c, d]

        List<String> names = List.of("John", "Alice");
        List<Integer> lengths = names.stream()
                .map(String::length)
                .toList();
        System.out.println(lengths); // [4, 5]

    }
}
