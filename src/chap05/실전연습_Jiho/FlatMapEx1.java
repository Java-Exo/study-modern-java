package chap05.실전연습_Jiho;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FlatMapEx1 {
    public static void main(String[] args) {
        List<String> words = Arrays.asList("Hello", "World");
        List<String> uniqueCharacters = words.stream()
                .map(word -> word.split(""))
                .flatMap(Arrays::stream)
                .distinct()
                .collect(Collectors.toList());


        for (String uniqueCharacter : uniqueCharacters) {
            System.out.print(uniqueCharacter+ " ");
        }
    }
}
