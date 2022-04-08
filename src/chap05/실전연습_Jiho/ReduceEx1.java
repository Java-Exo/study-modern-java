package chap05.실전연습_Jiho;

import java.util.Arrays;
import java.util.List;

public class ReduceEx1 {
    public static void main(String[] args) {
        List<Integer> numbers = Arrays.asList(4,5,3,9);
//        int sum = numbers.stream().reduce(0, (a,b) -> a+ b);
        int sum = numbers.stream().reduce(0, Integer::sum);
//        int product = numbers.stream().reduce(1, (a,b) -> a * b);
        int product = numbers.stream().reduce(1, (a,b) -> a * b);
        System.out.println("sum = " + sum);
        System.out.println("product = " + product);
    }
}
