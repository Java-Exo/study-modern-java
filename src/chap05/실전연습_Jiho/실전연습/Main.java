package chap05.실전연습_Jiho.실전연습;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Main {

    /*
    1. 2011년에 일어난 모든 트랜잭션을 찾아 값을 오름차순으로 정리하시오.
    2. 거래자가 근무하는 모든 도시를 중복 없이 나열하시오.
    3. 케임브리지에서 근무하는 모든 거래자를 찾아서 이름순으로 정렬하시오.
    4. 모든 거래자의 이름을 알파벳순으로 정렬해서 반환하시오.
    5. 밀라노에 거래자가 있는가?
    6. 케임브리지에 거주하는 거래자의 모든 트랜잭션값을 출력하시오.
    7. 전체 트랜잭션 중 최댓값은 얼마인가?
    8. 전체 트랜잭션 중 최솟값은 얼마인가?
     */

    public static void main(String[] args) {
        Trader raoul = new Trader("Raoul", "Cambridge");
        Trader mario = new Trader("Mario", "Milan");
        Trader alan = new Trader("Alan", "Cambridge");
        Trader brian = new Trader("Brian", "Cambridge");


        List<TransAction> transActions = Arrays.asList(
                new TransAction(brian, 2011, 300),
                new TransAction(raoul, 2012, 1000),
                new TransAction(raoul, 2011, 400),
                new TransAction(mario, 2012, 710),
                new TransAction(mario, 2012, 700),
                new TransAction(alan, 2012, 950)
        );
        // 1. 2011년에 일어난 모든 트랜잭션을 찾아 값을 오름차순으로 정리하시오.
        transActions.stream()
                        .filter(ta -> ta.getYear() == 2011)
                        .sorted(Comparator.comparing(TransAction::getValue))
                        .collect(Collectors.toList());


        // 2. 거래자가 근무하는 모든 도시를 중복 없이 나열하시오.
        List<String> collect = transActions.stream()
                .map(TransAction::getTrader)
                .map(Trader::getCity)
                .distinct()
                .collect(Collectors.toList());
        for (String s : collect) {
            System.out.print(s + " ");
        }

        System.out.println();

        // 3. 케임브리지에서 근무하는 모든 거래자를 찾아서 이름순으로 정렬하시오.
        List<Trader> collect1 = transActions.stream()
                .map(TransAction::getTrader)
                .filter(t -> "Cambridge".equals(t.getCity()))
                .sorted(Comparator.comparing(Trader::getName))
                .collect(Collectors.toList());

        for (Trader trader : collect1) {
            System.out.print(trader.getName() + " ");
        }

        // 4. 모든 거래자의 이름을 알파벳순으로 정렬해서 반환하시오.
        List<String> collect2 = transActions.stream()
                .map(TransAction::getTrader)
                .map(Trader::getName)
                .sorted()
                .collect(Collectors.toList());
        System.out.println();

        for (String s : collect2) {
            System.out.print(s + " ");
        }

        // 5. 밀라노에 거래자가 있는가?
        boolean b = transActions.stream()
                .map(TransAction::getTrader)
                .anyMatch(t -> "Milan".equals(t.getCity()));

        System.out.println();
        System.out.println(b);

        // 6. 케임브리지에 거주하는 거래자의 모든 트랜잭션값을 출력하시오.
        List<Integer> collect3 = transActions.stream()
                .filter(t -> "Cambridge".equals(t.getTrader().getCity()))
                .map(TransAction::getValue)
                .collect(Collectors.toList());
        for (Integer integer : collect3) {
            System.out.print(integer + " ");
        }

        //    7. 전체 트랜잭션 중 최댓값은 얼마인가?
        Optional<Integer> max = transActions.stream()
                .map(TransAction::getValue)
                .reduce(Integer::max);
        System.out.println(max.get());

        //    8. 전체 트랜잭션 중 최솟값은 얼마인가?
        Optional<Integer> min = transActions.stream()
                .map(TransAction::getValue)
                .reduce(Integer::min);

        System.out.println(min.get());
    }
}
