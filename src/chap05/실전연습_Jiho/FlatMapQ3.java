package chap05.실전연습_Jiho;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class FlatMapQ3 {
    public static void main(String[] args) {
        List<Integer> numbers1 = Arrays.asList(1, 2, 3);
        List<Integer> numbers2 = Arrays.asList(3, 4);

        List<int[]> pairs = numbers1.stream()
                .flatMap(i -> numbers2.stream()
                        .filter(j -> (i+j) % 3 == 0)
                        .map(j-> new int[]{i, j}))
                .collect(Collectors.toList());

        for (int[] pair : pairs) {
            System.out.println(Arrays.toString(pair));
        }
    }
}
