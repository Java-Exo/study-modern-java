Chapter 05 스트림 활용
==================

### 5.1 필터링 
### 5.1.1 프레디케이트로 필터링 
스트림 인터페이스는 filter 메서드를 지원한다. 

filter메서드는 **프레디케이트(불리언을 반환하는 함수)**를 인수로 받아서 프레디케이트와 일치하는 모든 요소를 포함하는 스트림을 반환한다.

``` java
List<Dish> vegetarianMenu = menu.stream()
                                .filter(Dish::isVegetarian) // 채식요리인지 확인하는 메서드 참조
                                .collect(toList());
```
### 5.1.2 고유 요소 필터링
스트림은 고유 요소로 이루어진 스트림을 반환하는 distinct 메서드도 지원한다(고유여부는 스트림에서 만든 객체의 hashCode, equals로 결정된다)


``` java
List<Integer> numbers = Arrays.asList(1, 2, 1, 3, 3, 2, 4);
numbers.stream()
       .filter(i -> i % 2 == 0)
       .distinct()
       .forEach(System.out::println);
```

## 5.2 스트림 슬라이싱

### 5.2.1 프레디케이트를 이용한 슬라이싱
자바 9은 스트림의 요소를 효과적으로 선택할 수 있도록 takeWhile, dropWhile 두 가지 새로운 메서드를 지원한다.

#### TAKEWHILE 활용

``` java
List<Dish> specialMenu = Arrays.asList(
    new Dish("seasonal fruit", true, 120, Dish.Type.OTHER),
    new Dish("prawns", false, 300, Dish.Type.FISH),
    new Dish("rice", true, 350, Dish.Type.OTHER),
    new Dish("chicken", false, 400, Dish.Type.MEAT),
    new Dish("french fries", true, 530, Dish.Type.OTHER),
```
320 칼로리 이하의 요리를 선택할 수 있을까?
``` java
List<Dish> filteredMenu = specialMenu.stream()
                                     .filter(dish -> dish.getCalories() < 320)
                                     .collect(toList()); 
```
takeWhile 연산을 이용하면 스트림을 포함한 무한 스트림을 포함한 모든 스트림에 프레디케이트를 적용해

스트림을 슬라이스 할 수 있다. 
``` java
List<Dish> filteredMenu = specialMenu.stream()
                                     .takeWhile(dish -> dish.getCalories() < 320)
                                     .collect(toList()); 
```
#### DROPWHILE 활용

나머지 요소를 선택하려면 어떻게 해야 할까? 즉 320 칼로리보다 큰 요소는 어떻게 탐색할까?
dropWhile을 이용해 작업을 완료할 수 있다. 
``` java
List<Dish> filteredMenu = specialMenu.stream()
                                     .dropWhile(dish -> dish.getCalories() < 320)
                                     .collect(toList()); 
```
dropWhile은 takeWhile과 정반대의 작업을 수행한다. dropWhile은 프레디케이트가 처음으로 거짓이 되는 지점까지 발견된 요소를 버린다. 

프레디케이트가 거짓이 되면 그 지점에서 작업을 중단하고 남은 모든 요소를 반환한다. dropWhile은 무한한 남은 요소를 가진 무한 스트림에서도 동작한다. 

### 5.2.2 스트림 축소
스트림은 주어진 값 이하의 크기를 갖는 새로운 스트림을 반환하는 limit(n) 메서드를 지원한다.

스트림이 정렬되어 있으면 최대 요소 n개를 반환할 수 있다. 

예를 들어 다음처럼 300칼로리 이상의 세 요리를 선택해서 리스트를 만들 수 있다.

``` java
List<Dish> filteredMenu = specialMenu.stream()
                                     .filter(dish -> dish.getCalories() < 320)
                                     .limit(3)
                                     .collect(toList()); 
```

### 5.2.3 요소 건너뛰기
스트림은 처음 n개 요소를 제외한 스트림을 반환하는 skip(n) 메서드를 지원한다.

n개 이하의 요소를 포함하는 스트림에 skip(n)을 호출하면 빈 스트림이 반환된다. 

limit(n)과 skip(n)은 상호 보완적인 연산을 수행한다. 
``` java
List<Dish> filteredMenu = specialMenu.stream()
                                     .filter(dish -> dish.getCalories() < 320)
                                     .skip(2)
                                     .collect(toList()); 
```

### 퀴즈 5-1 필터링
스트림을 이용해서 처음 등장하는 두 고기 요리를 필터링하시오. 

### 정답
``` java
List<Dish> dishes = menu.stream()
                        .filter(d -> d.getType() == Dish.Type.MEAT)
                        .limit(2)
                        .collect(toList()); 
```

## 5.3 매핑

특정 객체에서 특정 데이터를 선택하는 작업은 데이터 처리 과정에서 자주 수행되는 작업이다. 
예를 들어 SQL의 테이블에서 특정 열만 선택할 수 있다. 스트림 API의 map과 flatMap 메서드는 특정 데이터를 선택하는 기능을 제공한다. 

### 5.3.1 스트림의 각 요소에 함수 적용하기
스트림은 함수를 인수로 받는 map 메서드를 지원한다. 인수로 제공된 함수는 각 요소에 적용되며 함수를 적용한 결과가 새로운 요소로 매핑된다.

다음은 Dish::getName을 map 메서드로 전달해서 스트림의 요리명을 추출하는 코드다.
``` java
List<String> dishNames = menu.stream()
                             .map(dish::getName)
                             .collect(toList());
```
getName은 문자열을 반환하므로 map 메서드의 출력 스트림은 Stream<String> 형식을 갖는다. 


