Chapter 04 스트림 소개
==================

### 4.1 스트림이란 무엇인가?

스트림은 자바 8 API에서 새로 추가되었다. 

스트림을 이용하면 선언형으로 컬렉션 데이터를 처리할 수 있다. 

예제: 저칼로리의 요리명을 반환하고, 칼로리를 기준으로 요리를 정렬하는 자바 코드 

기존 코드 (자바 7)
``` java
List<Dish> lowCaloriesDishes = new ArrayList<>();
for (Dish dish: menu) {
    if (dish.getCalories() < 400) {
        lowCaloricDishes.add(dish);
    }
}
Collections.sort(lowCaloricDishes, new Comparator<Dish>() {  // 익명클래스 요리 정렬
    public int compare(Dish dish1, Dish dish2) {
        return Integer.compare(dish1.getCalories(), dish2.getCalories());
    }
});
List<String> lowCaloricDishesName = new ArrayList<>();
for(Dish dish: lowCaloriDishes) {
    lowCaloricDishesName.add(dish.getName());  // 정렬된 리스트를 처리하면서 요리 이름 선택
}
```

자바 8 코드
``` java
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;
List<String> lowCaloricDishesName = 
            menu.stream()
                .filter(d -> d.getCalories() < 400)  // 400 칼로리 이하의 요리 선택
                .sorted(comparing(Dishes::getCalories)) // 칼로리로 요리 정렬
                .map(Dish::getName)     // 요리명 추출
                .collect(toList());     // 모든 요리명을 리스트에 저장
```

stream() 을 parallelStream()으로 바꾸면 이 코드를 멀티코어 아키텍처에서 병렬로 실행 가능!

``` java
List<String> lowCaloricDishesName = 
            menu.parallelStream()
                .filter(d -> d.getCalories() < 400)  // 400 칼로리 이하의 요리 선택
                .sorted(comparing(Dishes::getCalories)) // 칼로리로 요리 정렬
                .map(Dish::getName)     // 요리명 추출
                .collect(toList());   
```

### 스트림이 주는 이득 

1. 선언형으로 코드 OK! 
    - 즉, 루프로와 if 조건문 등의 제어 블록을 사용해서 어떻게 동작을 구현할지 지정할 필요 없이 '저칼로리의 요리만 선택하라' 같은 동작의 수행을 지정할 수 있다. 
    - 선언형 코드와 동작 파라미터화를 활용하면 변하는 요구사항에 쉽게 대응 OK
2. filter, sorted, map, collect 같은 여러 빌딩 블록 연산을 연결해서 복잡한 데이터 처리 파이프라인을 만들 수 있다. 


### 자바 8 스트림 API 특징
* 선언형 : 더 간결하고 가독성이 좋아진다. 
* 조립할 수 있음 : 유연성이 좋아진다.
* 병렬화 : 성능이 좋아진다. 


### 4.2 스트림 시작하기 

>스트림의 정의 : **'데이터 처리 연산을 지원하도록 소스에서 추출된 연속된 요소'**

주요 단어
* 연속된 요소
* 소스
* 데이터 처리 연산

#### 스트림 2가지 중요 특징
1. 파이프라이닝(Pipelining): 
대부분의 스트림 연산은 스트림 연산끼리 연결해서 커다란 파이프라인을 구성할 수 있도록 스트림 자신을 반환한다. 
그 덕분에 게으름, 쇼트서킷 같은 최적화도 얻을 수 있다. 
2. 내부 반복
반복자를 이용해서 명시적으로 반복하는 컬렉션과 달리 스트림은 내부 반복을 지원한다. 

``` java
import static java.util.stream.Collectors.toList;
List<String> threeHighCaloricDishNames = 
    menu.stream() // 메뉴(요리 리스트)에서 스트림을 얻는다
        .filter(dish -> dish.getCalories() > 300) // 파이프라인 연산 만들기, 첫 번째로 고칼로리 요리를 필터링한다.
        .map(Dish::getName) // 요리명 추출
        .limit(3)    // 선착순 세 개만 선택
        .collect(toList());   // 결과를 다른 리스트로 저장
    System.out.prinln(threeHighCaloricDishNames); // 결과는 [pork, beef, chicken]이다.
```
우선 요리 리스트를 포함하는 menu에 stream 메서드를 호출해서 스트림을 얻었다. 

여기서 데이터 소스는 요리 리스트(메뉴)다. 데이터 소스는 연속된 요소를 스트림에 제공한다. 

다음으로 스트림에 filter, map, limit, collect로 이어지는 일련의 데이터 처리 연산을 적용한다. 

collect를 제외한 모든 연산은 서로 파이프라인을 형성할 수 있도록 스트림을 반환한다. 

파이프라인은 소스에 적용하는 질의 같은 존재다. 

마지막으로 collect 연산으로 파이프라인을 처리해서 결과를 반환한다.(collect는 스트림이 아니라 List를 반환한다.)


> 필터링 → filter, 추출 → map, 축소 → limit


### 4.3 스트림과 컬렉션
자바의 기존 컬렉션과 새로운 스트림 모두 연속된 요소 형식의 값을 저장하는 자료구조의 인터페이스를 제공한다. 

여기서 '연속된sequenced'이라는 표현은 순서와 상관없이 아무 값에나 접속하는 것이 아니라 순차적으로 값에 접근한다는 것을 의미한다.


>컬렉션과 스트림의 차이를 알아보자 

데이터를 **언제** 계산하느냐가 컬렉션과 스트림의 가장 큰 차이.

컬렉션은 현재 자료구조가 포함하는 모든 값을 메모리에 저장하는 자료구조다. 

즉, 컬렉션의 모든 요소는 컬렉션에 추가하기 전에 계산되어야 한다.

- 컬렉션에 요소를 추가하거나 컬렉션의 요소를 삭제할 수 있다. 이러한 연산을 수행할 때마다 컬렉션의 모든 요소를 메모리에 저장해야 하며 컬렉션에 추가하려는 요소는 미리 계산되어야 한다. 

<hr>
반면 스트림은 이론적으로 *요청할 때만 요소를 계산**하는 고정된 자료구조다(스트림에 요소를 추가하거나 스트림에서 요소를 제거할 수 없다)

사용자가 요청하는 값만 스트림에서 추출한다는 것이 핵심이다.

물론 사용자 입장에서는 이러한 변화를 알 수 없다. 

결과적으로 스트림은 생산자producer와 소비자consumer 관계를 형성한다. 

또한 스트림은 게으르게 만들어지는 컬렉션과 같다. 

즉, 사용자가 데이터를 요청할 때만 값을 계산한다. 



#### 4.3.1 딱 한 번만 탐색할 수 있다

반복자와 마찬가지로 스트림도 한 번만 탐색할 수 있다. 즉, 탐색된 스트림의 요소는 소비된다.

반복자와 마찬가지로 한 번 탐색한 요소를 탐색하려면 초기 데이터 소스에서 새로운 스트림을 만들어야 한다.

(그러려면 컬렉션처럼 반복 사용할 수 있는 데이터 소스여야 한다. 만일 데이터소스가 I/O 채널이라면 소스를 반복 사용할 수 없으므로 새로운 스트림을 만들 수 없다)

``` java
List<String> title = Arrays.asList("Java8", "In", "Action");
Stream<String> s = title.stream();
s.forEach(System.out::println); // title의 각 단어를 출력
s.forEach(System.out::println); // java.lang.illegalStateException: 스트림이 이미 소비되었거나 닫힘

```

> 스트림은 단 한 번만 소비할 수 있다!!! 는 점 명심하길 바래


#### 4.3.2 외부 반복과 내부 반복

**외부 반복** : 컬렉션 인터페이스를 사용하려면 사용자가 직접 요소를 반복해야 한다.

**내부 반복** : 스트림 라이브러리는 반복을 알아서 처리하고 결과 스트림 값을 어딘가에 저장해준다.

예제 4-1 컬렉션: for-each 루프를 이용하는 외부 반복
``` java
List<String> names = new ArrayList<>();
for (Dish dish : menu) {         // 메뉴 리스트를 명시적으로 순차 반복한다. 
   names.add(dish.getName());   // 이름을 추출해서 리스트에 추가한다. 
}
```
for-each 구문은 Iterator 객체를 이용하는 것보다 더 쉽게 컬렉션을 반복할 수 있다. 

예제 4-2 컬렉션: 내부적으로 숨겨졌던 반복자를 사용한 외부 반복
``` java
List<String> names = new ArrayList<>();
Iterator<String> iterator = menu.iterator();
while(iterator.hasNext()) {
   Dish dish = iterator.next();
   names.add(dish.getName());
}
```

예제 4-3 스트림: 내부 반복
``` java
List<String> names = menu.stream()
                     .map(Dish::getName)
                     .collect(toList()); // map메서드를 getName메서드로 파라미터화 해서 요리명을 추출한다. 
```

#### 내부 반복이 더 좋은 다른 두가지 이유
1. 작업을 투명하게 병렬로 처리
2. 더 최적화된 다양한 순서로 처리 가능 


스트림 라이브러리의 내부 반복은 데이터 표현과 하드웨어를 활용한 병렬성 구현을 자동으로 선택한다. 

반대로 for-each를 이용하는 외부 반복은 병렬성을 스스로 관리해야 함. 


### 4.4 스트림 연산

``` java
List<String> threeHighCaloricDishNames = 
    menu.stream() // 메뉴(요리 리스트)에서 스트림을 얻는다
        .filter(dish -> dish.getCalories() > 300) // 파이프라인 연산 만들기, 첫 번째로 고칼로리 요리를 필터링한다.
        .map(Dish::getName) // 요리명 추출
        .limit(3)    // 선착순 세 개만 선택
        .collect(toList());   // 결과를 다른 리스트로 저장
```

위 연산을 두 그릅으로 구분 가능
* filter, map, limit는 서로 연결되어 파이프라인을 형성한다. 
* collect로 파이프라인을 실행한 다음에 닫는다. 

* 연결할 수 있는 스트림 연산을 → 중간 연산intermediate operation 
* 스트림을 닫는 연산 → 최종 연산 terminal operation 

왜 스트림의 연산을 두 가지로 구분하는 것일까? 

### 4.4.1 중간 연산
filter나 sorted 같은 중간 연산은 다른 스트림을 반환한다. 따라서 여러 중간 연산을 연결해서 

질의를 만들 수 있다. 

중간 연산의 중요한 특징은 단말 연산을 스트림 파이프라인에 실행하기 전까지는 아무 연산도 수행하지 않는다는 것, 

즉 게으르다lazy는 것이다. 중간 연산을 합친 다음에 합쳐진 중간 연산을 최종 연산으로 한 번에 처리하기 때문이다. 

### 4.4.2 최종 연산
최종 연산은 스트림 파이프라인에서 결과를 도출한다. 보통 최종 연산에 의해 List, Integer, void 등 
스트림 이외의 결과가 반환된다. 

### 4.6 마치며
- 스트림은 소스에서 추출된 연속 요소로, 데이터 처리 연산을 지원한다. 
- 스트림은 내부 반복을 지원한다. 내부 반복은 filter, map, sorted등의 연산으로 반복을 추상화한다.
- 스트림에는 중간 연산과 최종 연산이 있다. 
- 중간 연산은 filter와 map처럼 스트림을 반환하면서 다른 연산과 연결되는 연산이다. 중간 연산을 이용해서 파이프라인을 구성할 수 있지만 중간 연산으로는 어떤 결과도 생성할 수 없다. 
- forEach나 count 처럼 스트림 파이프라인을 처리해서 스트림이 아닌 결과를 반환하는 연산을 최종 연산이라고 한다. 
- 스트림의 요소는 요청할 때 게으르게 lazily 계산된다. 