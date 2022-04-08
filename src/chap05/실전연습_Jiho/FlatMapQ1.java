package chap05.실전연습_Jiho;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class FlatMapQ1 {
    public static void main(String[] args) {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
        List<Integer> squares = numbers.stream()
                .map(n -> n * n)
                .collect(Collectors.toList());

        for (Integer square : squares) {
            System.out.print(square + " ");
        }
    }
}
