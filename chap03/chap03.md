chater 03 람다표현식
==================
동작파라미터화를 이용함.
정의한 코드블록을 다른 메서드로 전달 가능
더 유연하고 재사용할 수 있는 코드를 만들 수 있다. 

익명클래스로 다양한 동작 구현가능 but 코드가 깔끔하지 않음


### 람다표현식이란?
메서드로 전달할 수 있는 익명 함수를 단순화한 것

### 람다의 특징 
* 익명
  - 보통 메서드와 달리 이름이 없으므로 **익명**이라 표현한다. 구현해야 할 코드에 대한 걱정거리가 줄어든다. 
* 함수
  * 람다는 메서드처럼 특정 클래스에 종속되지 않으므로 함수라고 부른다. 하지만 메서드처럼 파라미터 리스트, 바디, 반환형식, 가능한 예외 리스트를 포함한다.
* 전달
  * 람다 표현식을 메서드 인수로 전달하거나 변수로 저장할 수 있다.
* 간결성
  * 익명 클래스처럼 많은 자질구레한 코드를 구현할 필요가 없다. 

람다라는 용어는 미적분학 학계에서 개발한 시스템에서 유래

### 람다표현식이 중요한 이유?
: 2장에서 확인한 자질구레한 코드를 간결하게 하여 코드를 전달할 수 있다. 

``` java
Comparator<Apple> byWeight = new Comparator<Apple>() {
    pulic int compare(Apple a1, Apple a2) {
        return a1.getWeight().compareTo(a2.getWeight());
    }
};
```

다음은 람다를 이용한 코드다. 
``` java
Comparator<Apple> byWeight = 
      (Apple a1, Apple a2) -> a1.getWeight().compareTo(a2.getWeight());
```
코드가 훨씬 간단해졌다. 

그림 3-1 람다 표현식은 파라미터, 화살표, 바디로 이루어진다. 

``` java
(Apple a1, Apple a2) -> a1.getWeight().compareTo(a2.getWeight());
    람다 파라미터     화살표      람다 바디   
```
* 파라미터 리스트
  - Comparator의 compare 메서드 파라미터(사과 두 개)
* 화살표
  * 화살표 (->)는 람다의 파라미터 리스트와 바디를 구분한다.
* 람다 바디
  * 두 사과의 무게를 비교한다. 람다의 반환값에 해당하는 표현식이다.

다음은 자바 8에서 지원하는 다섯 가지 람다 표현식 예제다.
``` java
1. (String s) -> s.length()
2. (Apple a) -> a.getWeight() > 150
3. (int x, int y) -> {
    System.out.println("Result:");
    System.out.println(x + y);
}
4. () -> 42 
5. (Apple a1, Apple a2) -> a1.getWeight().compareTo(a2.getWeight()) 
```

1. String 형식의 파라미터 하나를 가지며 int를 반환한다. 람다 표현식에는 return이 함축되어 있으므로 return문을 명시적으로 사용하지 않아도 된다. 
2. Apple형식의 파라미터 하나를 가지며 boolean(사과가 150그램 보다 무거운지 결정)을 반환한다.
3. int형식의 파라미터 두 개를 가지며 리턴값이 없다. 
4. 파라미터가 없으며 int 42를 반환한다.
5. Apple형식의 파라미터 두 개를 가지며 int(두 사과의 무게 비교 결과)를 반환한다.

자바 설계자는 이미 C#이나 스칼라 같은 비슷한 기능을 가진 다른 언어와 비슷한 문법을 자바에 적용하기로 했다.<br>
다음은 표현식 스타일expression style 람다라고 알려진 람다의 기본 문법이다. <br>

(parameters) -> expression

또는 다음과 같이 표현할 수 있다 (블록스타일 block-style)<br>

(parameters) -> { statements; }

#### 퀴즈 3-1 람다 문법
앞에서 설명한 람다 규칙에 맞지 않는 람다 표현식을 고르시오.

1. () -> {}
2. () -> "Raoul"
3. () -> { return "Mario"; }
4. (Integer i) -> return "Alan" + i;
5. (String s) -> { "Iron Man"; }

> 정답 <br>
> 4번과 5번이 유효하지 않는 람다 표현식이다.
1. 파라미터가 없으며 void를 반환하는 람다 표현식이다. 이는 public void run() {} 처럼 바디가 없는 메서드와 같다. 
2. 파라미터가 없으며 문자열을 반환하는 표현식이다. 
3. 파라미터가 없으며 (명시적으로 return문을 이용해서) 문자열을 반환하는 표현식이다.
4. return은 흐름 제어문이다. (Integer i) -> { return "Alan" + i; }처럼 되어야 올바른 람다 표현식이다.
5. "Iron Man"은 구문(statement)이 아니라 표현식(expression)이다. (String s) -> "Iron Man"처럼 되어야 올바른 람다 표현식이다. 또는 (String s) -> { return "Iron Man"; }처럼 명시적으로 return문을 사용해야 한다. 

### 어디에, 어떻게 람다를 사용할까?
2장에서 구현했던 필터 메서드에도 람다를 활용할 수 있었다. 
``` java
List<Apple> greenApples = filter(inventory, (Apple a) -> GREEN.equals(a.getColor()));
```

그러면 정확인 어디에서 람다를 사용할 수 있다는 건가? 

**함수형 인터페이스**라는 문맥에서 람다 표현식을 사용할 수 있다. 

지금부터 함수형 인터페이스가 무엇인지 알아보자. 

### 함수형 인터페이스 
2장에서 만든 Predicate<T> 인터페이스로 필터 메서드를 파라미터화할 수 있었음을 기억하는가?
바로 Predicate<T>가 함수형 인터페이스다. Predicate<T>는 오직 하나의 추상 매서드만 지정하기 때문이다. 
``` java
public interface Predicate<T> {
  boolean test (T t);
}
```
간단히 말해 **함수형 인터페이스**는 정확히 하나의 추상 메서드를 지정하는 인터페이스다. 지금까지 살펴본 자바 API의 함수형 인터페이스로 Comparator, Runnable등이 있다. 
``` java
public interface Comparator<T> {
  int compare(T o1, T o2);
}

public interface Runnable {
   void run();
}

pubic interface ActionListener extends EventListener {
  void actionPerformed(ActionEvent e);
}

public interface Callable<V> {
  V call() throws Exception;
}

public interface PrivilegedAction<T> {
  T run();
}
```
#### 퀴즈 3-2 함수형 인터페이스
다음 인터페이스 중 함수형 인터페이스는 어느 것인가?
``` java
public inteface Adder {
  int add(int a, int b);
}

public inteface SmartAdder extends Adder {
  int add(double a, double b);
}

public interface Nothing {
}
```
#### 정답
Adder만 함수형 인터페이스다.

SmartAdder는 두 추상 add 메서드(하나는 Adder에서 상속받음)를 포함하므로 함수형 인터페이스가 아니다.

Nothing은 추상 메서드가 없으므로 함수형 인터페이스가 아니다. 

### 함수형 인터페이스로 뭘 할 수 있을까?
람다표현식으로 함수형 인터페이스의 추상 메서드 구현을 직접 전달할 수 있으므로 **전체 표현식을 함수형 인터페이스의 인스턴스로 취급**(기술적으로 따지면 함수형 인터페이스를 구현한 클래스의 인스턴스)할 수 있다. 

함수형 인터페이스보다는 덜 깔끔하지만 익명 내부 클래스로도 같은 기능을 구현할 수 있다.

다음 예제는 Runnable이 오직 하나의 추상 메서드 run을 정의하는 함수형 인터페이스이므로 올바른 코드다. 

``` java
Runnable r1 = () -> System.out.println("Hello World 1"); // 람다 사용

Runnable r2 = new Runnable() { // 익명클래스 사용
  public void run() {
      System.out.println("Hello World 2");
  }
};

public static void process(Runnable r) {
  r.run();
}
process(r1);  // Hello World 1 출력
process(r2);  // Hello World 2 출력
// 직접 전달된 람다 표현식으로 "Hello World 3" 출력
process(() -> System.out.println("Hello World 3"));  
```

### 함수 디스크립터

함수형 인터페이스의 추상 메서드 시그니처signature는 람다 표현식의 시그니처를 가리킨다.<br>
람다 표현식의 시그니처를 서술하는 메서드를 **함수 디스크립터function descriptor** 라고 부른다.

``` java
public void process(Runnable r) {
  r.run();
}

process(() -> System.out.println("This is awesome!!"));

```
위 코드를 실행하면 'This is awesome!!'이 출력된다. () -> System.out.println("This is awesome!!")은 인수가 없으며 void를 반환하는 람다 표현식이다. 
이는 Runnable 인터페이스의 run메서드 시그니처와 같다., 


#### 퀴즈 3-3 어디에 람다를 사용할 수 있는가?
다음 중 람다 표현식을 올바로 사용한 코드는?

``` java
1. execute(() -> {});
  public void execute(Runnable r) {
      r.run();
  }
2. public callable<String> fetch() {
    return () -> "Tricky example ;-)";
  }
3. Predicate<Apple> p = (Apple a) -> a.getWeight();
```
1번과 2번은 유효한 람다 표현식이다. 

첫 번째 예제에서 람다 표현식 () -> {}의 시그니처는 () -> void며 Runnable의 추상 메서드 run의 시그니처와 일치하므로 유효한 람다 표현식이다. 다만 람다의 바디가 비어있으므로 이 코드를 실행하면 아무 일도 일어나지 않는다. 

두 번째 예제도 유효한 람다 표현식이다. fetch 메서드의 반환 형식은 Callable<String>이다. T를 String으로 대치했을 때 Callable<String>메서드의 시그니처는 () -> String이 된다. ()

세 번째 예제에서 람다표현식의 시그니처는 (Apple) -> (Integer)이므로 Predicate<Apple>:(Apple) -> boolean의 test메서드의 시그니처와 일치하지 않아 유효한 람다표현식이 아니다. 

#### @FunctionalInterface는 무엇인가? 
 함수형 인터페이스임을 가리키는 어노테이션이다. @FunctionalInterface를 선언했지만 실제로 함수형 인터페이스가 아니면 컴파일러가 에러를 발생시킨다. 

### 람다 활용 : 실행 어라운드 패턴 
자원처리(예.데이터베이스의 파일 처리)에 사용하는 순환 패턴은 자원을 열고, 처리한 다음에, 자원을 닫는 순서로 이루어진다. 설정과 정리 과정은 대부분 비슷하다. 

``` java
public String processFile() throws IOException {
  try( BufferedReader br = new BufferedReader(new FileReader("data.txt"))) {
      return br.readLine(); // 실제 필요한 작업을 하는 행이다. 
  }
}
```
실제 자원을 처리하는 코드를 설정과 정리 두 과정이 둘러싸는 형태를 갖는다. 
위와 같은 형식의 코드를 **실행 어라운드 패턴** 이라고 부른다.
### 실행어라운드 패턴 적용 과정

#### 1단계 : 동작 파라미터화를 기억하라.
processFile의 동작을 파라미터화한다. processFile메서드가 BufferedReader를 이용해서 다른 동작을 수행할 수 있도록 processFile메서드로 동작을 전달해야 한다.
processFile 메서드가 한 번에 두 행을 읽게 하려면 코드를 어떻게 고쳐야 할까?
우선 BufferedReader를 인수로 받아서 String을 반환하는 람다가 필요하다. 
다음은 BufferedReader에서 두 행을 출력하는 코드다.
``` java
String result = processFile((BufferedReader br) -> br.readLine() + br.readLine());
```
#### 2단계 : 함수형 인터페이스를 이용해서 동작 전달
함수형 인터페이스 자리에 람다를 사용할 수 있다. 따라서 BufferedReader -> String과 IOException을 던질 수 있는 시그니처와 일치하는 함수형 인터페이스를 만들어야 한다. 
``` java
@FunctionalInterface
public interface BufferedReaderProcessor {
    String process(BufferedReader b) throws IOException;
}
```
정의한 인터페이스를 processFile 메서드의 인수로 전달할 수 있다. 
``` java
public String processFile(BufferedReaderProcessor p) throws IOException {
  ...
}
```
#### 3단계 : 동작 실행
processFile 바디 내에서 BufferedReaderProcessor 객체의 process를 호출할 수 있다. 
``` java
public String processFile(BufferedReaderProcessor p) throws IOException {
   try( BufferedReader br = new BufferedReader(new FileReader("data.txt"))) {
      return p.process(br); // BufferedReader 객체 처리 
  }
}
```
#### 4단계 : 람다 전달

``` java
String oneLine = processFile((BufferedReader br) -> br.readLine());
```
``` java
String twoLines = processFile((BufferedReader br) -> br.readLine() + br.readLine());
```

### 함수형 인터페이스 사용
함수형 인터페이스 Predicate, Consumer, Function 인터페이스를 설명
#### Predicate
java.util.function.Predicate<T> 인터페이스는 test라는 추상 메서드를 정의하며 test는 제네릭 형식 T의 객체를 인수로 받아 불리언을 반환한다. 
다음 예제처럼 String 객체를 인수로 받는 람다를 정의할 수 있다. 
``` java
@FunctionalInterface
public interface Predicate<T> {
    boolean test(T t);
}
public <T> List<T> filter(List<T> list, Predicate<T> p) {
    List<T> results = new ArrayList<>();
    for (T t : list) {
        if(p.test(t)) {
          results.add(t);
        }
    }
    return results;
}
Predicate<String> nonEmptyStringPredicate = (String s) -> !s.isEmpty();
List<String> nonEmpty = filter(listOfStrings, nonEmptyStringPredicate);      
```

#### Consumer
java.util.function.Consumer<T> 인터페이스는 제네릭 형식 T 객체를 받아서 void를 반환하는 accept라는 추상 메서드를 정의한다. 

다음은 forEach 와 람다를 이용해서 리스트의 모든 항목을 출력하는 예제다. 
``` java
@FunctionalInterface
public interface Consumer<T> {
    boolean accept(T t);
}
public <T> void foreach(List<T> list, Consumer<T> c) {
    for (T t : list) {
        c.accept(t);
    }
}
forEach(
        Arrays.asList(1, 2, 3, 4, 5),
        (Integer i) -> System.out.println(i)
);

```
#### Function
java.util.function.Function<T,R> 인터페이스는 제네릭 형식 T를 인수로 받아서 제네릭 형식 R 객체를 반환하는 추상 메서드 apply를 정의한다. 
다음은 String 리스트를 인수로 받아 각 String의 길이를 포함하는 Integer리스트로 변환하는 map 메서드를 정의하는 예제다. 
``` java
@FunctionalInterface
public interface Function<T, R> {
    R apply(T t);
}
public <T, R> List<R> map(List<T> list, Function<T,R> f) {
    for (T t : list) {
        result.add(f.apply(t));
    }
    return result;
}

// [7, 2, 6]
List<Integer> l = map(
        Arrays.asList("lambdas", "in", "action"),
        (String s) -> s.length() // Function의 apply 메서드를 구현하는 람다
);
```

#### 기본형 특화

제네릭 파라미터에는 참조형만 사용할 수 있다. (예.Byte, Integer, Object, List)
자바에서는 기본형을 참조형으로 변환하는 기능을 제공한다. 이 기능을 **박싱**이라고 한다. 
참조형을 기본형으로 변환하는 반대 동작을 **언박싱**이라고 한다.
또한 박싱과 언박싱이 자동으로 이루어지는 **오토박싱**이라는 기능도 제공한다. 
예를 들어 다음은 유효한 코드다. (int가 Integer로 박싱됨)
``` java
List<Integer> list = new ArrayList<>();
for (int i = 300; i < 400; i++) {
  list.add(i);
}
```
하지만 이런 변환과정은 비용이 소모된다. 박싱한 값은 기본형을 감싸는 래퍼며 힙에 저장된다.
따라서 박싱한 값은 메모리를 더 소비하며 기본형을 가져올 때도 메모리를 탐색하는 과정이 필요하다. 

<br>
자바 8에서는 기본형을 입출력으로 사용하는 상황에서 오토박싱 동작을 피할 수 있도록 특별한 버전의 함수형 인터페이스를 제공한다. 
예를 들어 아래 예제에서 IntPredicate는 1000이라는 값을 박싱하지 않지만, `Predicate<Integer>`는 1000이라는 값을 Integer객체로 박싱한다. 

``` java
public interface IntPredicate {
    boolean test(int t);
}

IntPredicate evenNumbers = (int i) -> i % 2 == 0;
evenNumbers.test(1000); // 참 (박싱 없음)

Predicate<Integer> oddNumbers = (Integer i) -> i % 2 != 0;
oddNumbers.test(1000);    // 거짓 (박싱)
```

#### 퀴즈 3-4 함수형 인터페이스 
다음과 같은 함수형 디스크립터(즉, 람다 표현식의 시그니처)가 있을 때 어떤 함수형 인터페이스를 사용할 수 있는가? 또한 이들 함수형 인터페이스에 사용할 수 있는 유효한 람다 표현식을 제시하시오.

1. T -> R
2. (int, int) -> int
3. T -> void
4. () -> T
5. (T, U) -> R

##### 정답
1. Function<T,R>이 대표적인 예제다. T형식의 객체를 R형식의 객체로 변환할 때 사용한다. (예를 들면 Function<Apple, Integer>로 사과의 무게정보를 추출할 수 있다.)
2. IntBinaryOperator는 (int, int) -> int 형식의 시그니처를 갖는 추상 메서드 applyAsInt를 정의한다. 
3. Consumer<T>는 T -> void 형식의 시그니처를 갖는 충상 메서드 accept를 정의한다. 
4. Supplier<T>는 () -> T 형식의 시그니처를 갖는 추상 메서드 get을 정의한다. 또한 Callable<T>도 () -> T 형식의 시그니처를 갖는 추상 메서드 call을 정의한다. 
5. BitFunction<T,U,R>은 <T,U> -> R 형식의 시그니처를 갖는 추상 메서드 apply를 정의한다. 


### 예외, 람다, 함수형 인터페이스의 한계

함수형 인터페이스는 확인된 예외를 던지는 동작을 허용하지 않는다. 즉, 예외를 던지는 람다 표현식을 만들려면 확인된 예외를 선언하는 함수형 인터페이스를 직접 정의하거나 람다를 try/ catch 블록으로 감싸야 한다. 
...


### 3.5 형식 검사, 형식 추론, 제약은 생략 


#### 클로저

클로저란 함수의 비지역 변수를 자유롭게 참조할 수 있는 함수의 인스턴스를 가리킨다. 
예를 들어 클로저를 다른 함수의 인수로 전달할 수 있다. 

### 3.6 메서드 참조
메서드 참조를 이용하면 기존의 메서드 정의를 재활용해서 람다처럼 전달할 수 있다.때로는 람다 표현식보다 메서드 참조를 사용하는 것이 더 가독성이 좋으며 자연스러울 수 있다. 
다음은 메서드 참조와 새로운 자바 8 API를 활용한 정렬 예제다. 

다음은 기존 코드다. 
``` java
inventory.sort((Apple a1, Apple a2) -> a1.getWeight().compareTo(a2.getWeight()));
```

다음은 메서드 참조와 java.util.Comparator.comparing을 활용한 코드다. 
``` java
inventory.sort(comparing(Apple::getWeight));  // 첫 번째 메서드 참조!
```


#### 메서드 참조가 왜 중요한가?
메서드 참조는 특정 메서드만을 호출하는 람다의 축약형이라고 생각할 수 있다. 예를 들어 람다가 '이 메서드를 직접 호출해'라고 명령한다면 메서드를 어떻게 호출해야 하는지 설명을 
참조하기보다는 메서드명을 직접 참조하는 것이 편리하다. 
명시적으로 메서드명을 참조함으로써 **가독성을 높일 수 있다**.

#### 메서드 참조는 어떻게 활용할까?
메서드명 앞에 구분자(::)를 붙이는 방식으로 메서드 참조를 활용할 수 있다. 예를 들어 Apple::getWight는 Apple클래스에 정의된 getWeight의 메서드 참조다. 
실제로 메서드를 호출하는 것은 아니므로 괄호는 필요 없음을 기억하자. 결과적으로 메서드 참조는 (Apple a) -> a.getWeight()를 축약한 것이다. 

| 람다                                  | 메서드 참조 단축표현                        |
|-------------------------------------|------------------------------------|
| (Apple apple) -> apple.getWeight()  | Apple::getWeight                   |
| Thread.currentThread().dumpStack()  | Thread::currentThread()::dumpStack |
| (str,i) -> str.subString(i)         | String::subString                  |
| (String s) -> System.out.println(s) | System.out::println                |
| (String s) -> this.inValidName(s)   | this::isValidName|                  |
메서드 참조를 새로운 기능이 아니라 하나의 메서들를 참조하는 람다를 편리하게 표현할 수 있는 문법으로 간주할 수 있다. 메서드 참조를 이용하면 같은 기능을 더 간결하게 구현할 수 있다.

``` java
List<String> str = Arrays.asList("a", "b", "A", "B");
str.sort((s1,s2) -> s1.compareToIgnoreCase(s2));
```

메서드 참조를 사용해서 다음처럼 줄일 수 있다. 
``` java
List<String> str = Arrays.asList("a", "b", "A", "B");
str.sort(String::compareToIgnoreCase);
```

#### 퀴즈 3-6 메서드 참조
다음의 람다 표현식과 일치하는 메서드 참조를 구현하시오. <br>
`1. ToIntFunction<String> stringToInt = (String s) -> Integer.parseInt(s);`<br>
`2. BiPredicate<List<String>, String> contains = (list, element) -> list.contains(element);`<br>
`3. Predicate<String> startsWithNumber = (String s) -> this.startsWithNumber(string);`<br>

1. Function<String, Integer> stringToInteger = Integer::parseInt;
2. BiPredicate<List<String>, String> contains = List::contains;
3. Predicate<String> startsWithNumber = this::startsWithNumber;

### 3.6.2 생성자 참조 
ClassName::new 처럼 클래스명과 new키워드를 이용해서 기존 생성자의 참조를 만들 수 있다. 

예를 들어 인수가 없는 생성자, 즉 Supplier의 () -> Apple과 같은 시그니처를 갖는 생성자가 있다고 가정하자. 
``` java
Supplier<Apple> c1 = Apple::new;
Apple a1 = c1.get();  // Supplier의 get메서드를 호출해서 새로운 Apple객체를 만들 수 있다. 
```
위 예제는 다음 코드와 같다. 
``` java
Supplier<Apple> c1 = () -> new Apple(); // 람다 표현식은 디폴트 생성자를 가진 Apple을 만든다. 
Apple a1 = c1.get();  // Supplier의 get메서드를 호출해서 새로운 Apple객체를 만들 수 있다. 
```
Apple(Integer weight)라는 시그니처를 갖는 생성자는 Function 인터페이스의 시그니처와 같다. 따라서 다음과 같은 코드를 구현할 수 있다. 
``` java
Function<Integer, Apple> c2 = Apple::new; // Apple(Integer weight)의 생성자 참조
Apple a2 = c2.apply(110);     // Function의 apply메서드에 무게를 인수로 호출해서 새로운 Apple객체를 만들 수 있다. 
```


#### 마치며
* 람다 표현식은 익명함수의 일종. 이름은 없지만, 파라미터 리스트, 바디, 반환 형식을 가지며 예외를 던질 수 있다. 
* 람다 표현식으로 간결한 코드 구현 가능
* 함수형 인터페이스는 하나의 추상 메서드만을 정의하는 인터페이스다. 
* 함수형 인터페이스를 기대하는 곳에서만 람다 표현식을 사용할 수 있다. 
* 람다 표현식을 이용해서 함수형 인터페이스의 추상 메서드를 즉석으로 제공할 수 있으며, **람다 표현식 전체가 함수형 인터페이스의 인스턴스로 취급된다.**

