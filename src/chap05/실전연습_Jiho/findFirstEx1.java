package chap05.실전연습_Jiho;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class findFirstEx1 {
    public static void main(String[] args) {
        List<Integer> someNumbers = Arrays.asList(1, 2, 3, 4, 5);
        Optional<Integer> firstSquareDivisibleByThree = someNumbers.stream()
                .map(n -> n* n)
                .filter(n -> n % 3 == 0)
                .findFirst();  // 9

        System.out.println(firstSquareDivisibleByThree.get());
    }
}
