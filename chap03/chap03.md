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

