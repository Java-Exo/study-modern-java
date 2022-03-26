## chapter02 동작 파라미터화 코드 전달하기

---------------

#### 동작 파라미터화
* 동작 파라미터화란 아직은 어떻게 실행할 것인지 결정하지 않은 코드 블록을 의미한다
* 이 코드 블록은 나중에 프로그램에서 호출한다(코드 블록의 실행은 나중이다)
  * 나중에 실행될 메서드의 인수로 코드 블록을 전달 할 수 있다
  * 코드 블록에 따라 메서드의 동작이 파라미터화 된다
* 동작 파라미터화를 이용하면 자주 변하는 요구사항에 효과적으로 대응 할 수 있다

#### 변화하는 요구사항에 대응하기

```` java
1. 녹색 사과 필터링

enum Color { RED , GREEN }

public static List<Apple> filterGreenApples(List<Apple> inventory) {
  List<Apple> result = new ArrayList<>();
  
  for (Apple apple : inventory) {
    //녹색 사과 선택
    if(GREEN.equals(apple.getColor()) {
      result.add(apple);
    }
  }
  return result;
}
````
위 예제에서 사과의 색상이 변경 된다면 if 문에 조건을 추가 하던지 위 메서드를 복사해서 조건을 바꾸거나 할 수 있다

하지만 요구사항이 더 늘어나고 복잡해진다면 위와 같은 방법으론 대응하기가 힘들어진다

이런 경우 반복되는 코드를 추상화 시킨다

```` java
2. 색을 파라미터화 한다

public static List<Apple> filterApplesByColor(List<Apple> inventory, Color color) {
  List<Apple> result = new ArrayList<>();
  
  for (Apple apple : inventory) {
    if (apple.getColor().equals(color)) {
      result.add(apple);
    }
  }
  
  return result;
}

List<Apple> greenApples = filterAppleByColor(inventory, GREEN);
List<Apple> redApples = filterAppleByColor(inventory, RED);

````

두번째 예제와 같이 코드를 개선하면 다른 색상이 추가 되더라도 변화에 대응이 된다

하지만 색상이 아닌 다른 무게와 같은 다른 요구사항이 생긴다면 어떻게 해야될까?

위와 비슷한 무게를 판단하는 메서드를 만들면 해결은 될 것이다 

하지만 무게를 판단하는 메서드와 색을 판단하는 메서드는 대부분의 코드가 중복 될 것이다 

이런 경우 소프트웨어 공학의 DRY (don't repeat yourself 같은 것을 반복하지 말 것) 이라는 원칙을 어기는 것이다

물론 다른 방법도 있다 , 무게를 필터링 하는 메서드를 복사 하는것이 아닌 두 메서드를 하나로 합치고 플래그를 주는것이다 

```` java

3. 가능한 모든 속성으로 필터링

public static List<Apple> filterApples(List<Apple> inventory, Color color, int weight, boolean flag) {
  List<Apple> result = new ArrayList<>();
  
  for (Apple apple : inventory) {
    if ((flag && apple.getColor().equals(color) ||
        (!flag && apple.getWeight() > weight)) {
        result.add(apple);
    }
  }
  
  return result;
}

List<Apple> greenApples = filterApples(inventory, GREEN, 0, true);
List<Apple> redApples = filterApples(inventory, RED, 150, false);

````

3번째 예제처럼 필터링 메서드를 하나로 합치고 플래그로 구분하면 일단 코드의 가독성이 굉장히 떨어진다
* 먼저 클라이언트 코드에서의 true 와 false의 의미를 알 수 없다
* 만약 여기서 무게와 색이 아닌 다른 요구사항이 추가 된다면 어떻게 해야될까?? 코드는 점점 더 더러워질것이다
* 위와 같은 방식은 절대 사용하면 안되며 유지보수를 힘들게 한다

#### 동작 파라미터 적용
이제  파라미터를 추가하여 요구사항을 처리하던 예제들을 이번장의 주제인 동작 파라미터화를 이용하여 개선시켜보자

먼저 사과의 속성에 따라 참 거짓을 반환하는 프리케이트 함수를 만들어보자

```` java

선택 조건을 결정하는 인터페이스

//알고리즘 패밀리
public interface ApplePredicate {
  boolean test (Apple apple);
}

test 라는 동작을 수행할 인터페이스를 선언한다

위에서 if문으로 분기했던 선택조건을 구현체로 선언한다

//무거운 사과 선택(전략)
public class AppleHeavyWeightPredicate implements ApplePredicate {
  public boolean test(Apple apple) {
    return apple.getWeight() > 150;
  }
}

//녹색 사과 선택(전략)
public class AppleGreenColorPredicate implements ApplePredicate {
  public boolean test(Apple apple) {
    return GREEN.equals(apple.getColor());
  }
}

````
위 처럼 각 알고리즘(전략)을 캡슐화하는 알고리즘 패밀리를 정의해두고 런타임에 알고리즘을 선택하는 기법을 전략패턴이라고 부른다

이제 위에서 파라미터로 받던 예제를 고쳐보자

```` java

4. 추상적 조건으로 필터링

public static List<Apple> filterApples(List<Apple> inventory, ApplePredicate p) {
  List<Apple> result = new ArrayList<>();
  
  for (Apple apple : inventory) {
    if (p.test(apple)) {
        result.add(apple);
    }
  }
  
  return result;
}

List<Apple> greenApples = filterApples(inventory, new AppleGreenColorPredicate());
List<Apple> weightApples = filterApples(inventory, new AppleHeavyWeightPredicate());

````
위 처럼 코드를 변경하면 filterApples 메서드에서 사과 필터링에 관한 로직을 알 필요가 없게된다
* filterApples 메서드에서는 단순히 컬렉션을 반복하는 일을 수행할 뿐이다
따라서 코드의 유연성이 증대되어 추가 요구사항에 유연하게 대처가 가능해질것이며 가독성 또한 좋아졌다
* 위와 같은 코드는 클라이언트에서 전달하는 ApplePredicate 객체에 의해서 filterApples 메서드의 동작이 변화한다
  * 하나의 메서드(test 메서드)가 다른 동작을 수행하도록 재활용이 가능하다
  * 이러한 유연성 덕분에 동작 파라미터화는 유연한 API 설계시 중요하다
* 4번 예제는 메서드의 동작을 파라미터 했다고 볼 수 있다 
* 4번 예제에서 가장 중요한 부분은 test 메서드이다 
* fileterApples 메서드에서 사실 ApplePredicate 객체를 전달하는 이유는 메서드가 객체만을 인수로 받기 때문에 test 메서드를 사용하기 위해서이다

#### 복잡한 과정 간소화

4번째 예제는 동작 파라미터를 적용하여 유연성은 확보 하였지만 아직도 불편한 점이 남아있다

바로 요구사항이 추가 되는 경우 클래스를 계속 만들어 새로운 인스턴스를 생성해야 한다는 점이다

이번엔 익명 클래스를 사용해 새로운 클래스를 만들지 않도록 코드를 개선해보자
* 익명클래스 : 지역 클래스(블록 내부에 선언된 클래스)와 비슷한 개념으로 이름이 없는 클래스이다, 클래스 선언과 인스턴스화가 동시에 이루어질수있다

```` java
5. 익명 클래스 사용

//메서드 호출 시 익명 클래스를 사용해 구현체를 직접 만들어서 filterApples 메서드의 동작을 직접 파라미터화 했다
List<Apple> redApples = filterApples(inventory, new ApplePredicate () {
  public boolean test (Apple apple) {
    return RED.equals(apple.getColor());
  }
});
````

5번째 예제처럼 익명 클래스를 만들어서 사용하면 새로운 클래스를 만들지 않아도 되지만 가독성이 떨어지게되며 많은 공간을 차지한다

또한 많은 개발자들이 익명 클래스에 익숙하지 않다

이러한 부분을 람다를 사용하여 개선해보자

```` java
6. 람다 표현식 사용

// 람다를 사용하니 코드의 가독성이 굉장히 좋아졌다
List<Apple> redApples = filterApples(inventory, (Apple apple) -> RED.equals(apple.getColor()));
````

이제 마지막으로 구현부도 리스트 형식으로 추상화 후 클라이언트 코드에서 람다를 적용해보자

```` java
7. 리스트 형식으로 추상화

public interface Predicate<T> {
  boolean test(T t);
}

public static <T> List<T> filter(List<T> list, Predicate<T> p) {
  List<T> result = new ArrayList<>();
  
  for(T e: list) {
    if(p.test(e)) {
      result.add(e);
    }
  }
  
  return result;
}

List<Apple> redApples = filter(inventory, (Apple apple) -> RED.equals(apple.getColor());
List<Integer> evenNUmbers = filter(numbers, (Integer i) -> i % 2 == 0);
````

Apple 타입의 List 만 순회 할 수 있던 기존 filterApples 메서드를 filter 제네릭을 사용하여 메서드로 만들었다

위와 같이 추상화 하면 타입에 상관없이 클라이언트 코드에서 첫번째 인자로 넘겨준 list를 두번째 인자로 넘겨준 람다 표현식에 의해 필터링 하여 필터링된 list를 반환하게 된다
* 코드가 굉장히 간결해졌으며 유연성 또한 확보되었다
* 동작 파라미터화 패턴은 동작을 한조각의 코드로 캡슐화 한 다음 메서드로 전달해 메서드의 동작을 파라미터화 한다

#### 동작 파라미터화에서는 메서드 내부적으로 다양한 동작을 수행할 수 있도록 코드를 메서드 인수로 전달한다
#### 동작 파라미터화를 이용하면 변화하는 요구사항에 더 잘 대응할 수 있는 코드를 구현할 수 있으며 나중에 엔지니어링 비용을 줄일 수 있다
#### 코드 전달 기법을 이용하면 동작을 메서드의 인수로 전달 할 수 있다
#### 자바 8 이전에는 코드 전달 기법을 이용하면 코드가 지저분해졌지만 자바 8에서는 해당 문제를 개선하는 방법이 등장했다
#### 자바 API의 많은 메서드는 정렬, 스레드, GUI 처리 등을 포함한 다양한 동작으로 파라미터화를 할 수 있다

