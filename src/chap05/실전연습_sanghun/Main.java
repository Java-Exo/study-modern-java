package chap05.실전연습_sanghun;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

public class Main {
    /**
     *
     * 1. 2011년에 일어난 모든 트랜잭션을 찾아 값을 오름차순으로 정리하시오
     *
     * 2. 거래자가 근무하는 모든 도시를 중복 없이 나열하시오
     *
     * 3. 케임브리지에서 근무하는 모든 거래자를 찾아서 이름순으로 정렬하시오
     *
     * 4. 모든 거래자의 이름을 알파벳순으로 정렬해서 반환하시오
     *
     * 5. 밀라노에 거래자가 있는가?
     *
     * 6. 케임브리지에 거주하는 거래자의 모든 트랜잭션 값을 출력하시오
     *
     * 7. 전체 트랜잭션 중 최댓값은 얼마인가?
     *
     * 8. 전체 트랜잭션 중 최솟값은 얼마인가?
     *
     */
    Trader raoul = new Trader("Raoul", "Cambridge");
    Trader mario = new Trader("Mario", "Milan");
    Trader alan = new Trader("Alan", "Cambridge");
    Trader brian = new Trader("Brian", "Cambridge");

    List<Transaction> transactions = Arrays.asList(
            new Transaction(brian,2011,300),
            new Transaction(raoul,2012,1000),
            new Transaction(raoul,2011,400),
            new Transaction(mario,2012,710),
            new Transaction(mario,2012,700),
            new Transaction(alan,2012,950)
    );

    public Optional<Integer> 문제_8() {
        Optional<Integer> minValue = transactions.stream()
                .map(Transaction::getValue)
                .reduce(Integer::min);
        return minValue;
    }

    public Optional<Integer> 문제_7() {
        Optional<Integer> maxValue = transactions.stream()
                .map(Transaction::getValue)
                .reduce(Integer::max);
        return maxValue;
    }

    public void 문제_6() {
       transactions.stream()
                .filter(tr -> "Cambridge".equals(tr.getTrader().getCity()))
                .map(Transaction::getValue)
                .forEach(System.out::println);
    }

    public boolean 문제_5() {
        boolean milanBased =
                transactions.stream().anyMatch(tr -> tr.getTrader()
                                   .getCity()
                                   .equals("Milan")
                );
        return milanBased;
    }

    public String 문제_4() {
      String traderStr = transactions.stream()
                .map(tr -> tr.getTrader().getName())
                .distinct()                                  // 중복 제거
                .sorted()                                   // 알파벳 순서로 정렬
                .reduce("", (n1, n2) -> n1 + n2);   // 모든 문자열을 반복적으로 연결해서 새로운 문자열 객체를 만든다
        return traderStr;
    }

    public List<Trader> 문제_3() {
        List<Trader> nameSortTraders =
                transactions.stream()
                            .map(Transaction::getTrader)
                            .filter(trader -> "Cambridge".equals(trader.getCity()))
                            .sorted(Comparator.comparing(Trader::getName))
                            .collect(toList());
        return nameSortTraders;
    }

    public List<String> 문제_2() {
        List<String> citys =
                transactions.stream()
                            .map(tr -> tr.getTrader().getCity())
                            .distinct()
                            .collect(toList());
        return citys;
    }

    public List<Transaction> 문제_1() {
        List<Transaction> transactions2011 =
                transactions.stream()
                            .filter(tr -> tr.getYear() == 2011)
                            .sorted(Comparator.comparing(Transaction::getValue))
                            .collect(toList());

        return transactions2011;
    }


}
