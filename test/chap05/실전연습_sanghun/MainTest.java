package chap05.실전연습_sanghun;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MainTest {

    private Main main;

    @BeforeEach
    void setUp() {
        main = new Main();
    }

    @Test
    void 문제_1() {
        Trader raoul = new Trader("Raoul", "Cambridge");
        Trader brian = new Trader("Brian", "Cambridge");

        List<Transaction> transactions = Arrays.asList(
                new Transaction(brian,2011,300),
                new Transaction(raoul,2011,400)
        );

        //1. 2011년에 일어난 모든 트랜잭션을 찾아 값을 오름차순으로 정리하시오
        assertThat(transactions).isEqualTo(main.문제_1());
    }

    @Test
    void 문제_2() {
        List<String> citys = Arrays.asList(
                "Cambridge",
                "Milan"
        );
        //2. 거래자가 근무하는 모든 도시를 중복 없이 나열하시오
        assertThat(main.문제_2()).isEqualTo(citys);
    }

    @Test
    void 문제_3() {
        List<Trader> traders = Arrays.asList(
          new Trader("Alan","Cambridge"),
          new Trader("Brian","Cambridge"),
          new Trader("Raoul","Cambridge"),
          new Trader("Raoul","Cambridge")
        );

        //3. 케임브리지에서 근무하는 모든 거래자를 찾아서 이름순으로 정렬하시오
        assertThat(main.문제_3()).isEqualTo(traders);
    }

    @Test
    void 문제_4() {
        //4. 모든 거래자의 이름을 알파벳순으로 정렬해서 반환하시오
        assertThat(main.문제_4()).isEqualTo("AlanBrianMarioRaoul");
    }

    @Test
    void 문제_5() {
        //5. 밀라노에 거래자가 있는가?
        assertTrue(main.문제_5());
    }

    @Test
    void 문제_6() {
        OutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        //6. 케임브리지에 거주하는 거래자의 모든 트랜잭션 값을 출력하시오
        main.문제_6();
        assertThat(out.toString()).isEqualTo("300\r\n1000\r\n400\r\n950\r\n");
    }

    @Test
    void 문제_7() {
        Optional<Integer> optionalInteger = Optional.of(1000);

        //7. 전체 트랜잭션 중 최댓값은 얼마인가?
        assertEquals(optionalInteger,main.문제_7());
    }

    @Test
    void 문제_8() {
        Optional<Integer> optionalInteger = Optional.of(300);

        //8. 전체 트랜잭션 중 최솟값은 얼마인가?
        assertEquals(optionalInteger,main.문제_8());
    }

}