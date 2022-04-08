# Chapter 05 스트림 활용

----------------------

### 필터링
스트림의 요소를 선택하는 방법, 즉 프리디케이트 필터링 방법과 고유 요소만 필터링 하는 방법을 알아보자

#### 프리디케이트로 필터링
* 스트림 인터페이스는 filter 메서드를 지원한다
* filter 메서드는 프레디케이트를 인수로 받아서 프리디케이트와 일치하는 모든 요소를 포함하는 스트림을 반환한다
  * 프레디케이트 : 불리언을 반환하는 함수

```` java
모든 채식요리를 필터링해서 체식 메뉴를 만들어보자

List<Dish> vegetarianMenu = menu.stream()
                                .filter(Dish::isVegetarian)   // 채식 요리인지 확인하는 메서드 참조
                                .collet(toList());
````

#### 고유 요소 필터링
* 스트림은 고유 요소로 이루어진 스트림을 반환하는 distinct 메서드도 지원한다
  * 고유 여부는 스트림에서 만든 객체의 hashCode나 equals로 결정

```` java
리스트의 모든 짝수를 선택하고 중복을 필터링한다

List<Integer> numbers = Arrays.asList(1, 2, 2, 3, 3, 2, 4);
numbers.stream()
       .filter(i -> i % 2 == 0)
       .distinct()
       .forEach(System.out::println); 
````

### 스트림 슬라이싱
스트림의 요소를 선택하거나 스킵하는 다양한 방법을 알아보자

#### 프레디케이트를 이용한 슬라이싱
* 스트림의 요소를 효과적으로 선택할 수 있도록 자바 9는 takeWhile, dropWhile 메서드를 새로 선보였다

```` java
특별한 요리 목록

List<Dish> specialMenu = Arrays.asList(
  new Dish("seasonal fruit", true, 120, Dish.Type.OTHER),
  new Dish("prawns", false, 300, Dish.Type.FISH),
  new Dish("rice", true, 350, Dish.Type.OTHER),
  new Dish("chicken", false, 400, Dish.Type.MEAT),
  new Dish("french fries", true, 530, Dish.Type.OTHER);
  
);

````
##### TAKEWHILE 활용
```` java
320칼로리 이하의 요리를 찾아보자

앞에서 배운대로 filter를 사용해서 아래처럼 찾을 수 있다
List<Dish> filteredMenu = specialMenu.stream()
                                     .filter(dish -> dish.getCalories() < 320)
                                     .collect(toList());
                                     
filter 연산을 이용하면 전체 스트림을 반복하면서 각 요소에 프레디케이트를 적용하게 된다

위 리스트는 이미 칼로리 순으로 정렬되어 있는데 이 사실을 이용하여 320 칼로리보다 크거나 같은 요리가 나오는 경우 반복을 중단 시키면 더 효율적이다
(물론 작은 규모의 스트림이라면 큰 차이가 없겠지만 스트림의 크기가 크다면 차이가 많이 날것이다)

그럼 위에서 말한대로 하려면 어떻게 해야할까?

takeWhile 연산을 이용하여 아래와 같이 짜면된다

List<Dish> slicedMenu1 = specialMenu.stream()
                                    .takeWhile(dish -> dish.getCalories() 320)
                                    .collect(toList());

takeWhile을 이용하면 무한 스트림을 포함한 모든 스트림에 프레디케이트를 적용해 스트림을 슬라이스 할 수 있다

````

##### DROPWHILE 활용
```` java
320보다 큰 요소를 찾아보자
(320 칼로리 이하의 요리를 제외한 나머지 요리를 찾아보자)

List<Dish> slicedMenu2 = specialMenu.stream()
                                    .dropWhile(dish -> dish.getCalories() < 320)
                                    .collect(toList());
                                    
dropWhile은 takeWhile과 정반대의 작업을 수행한다

dropWhile은 프레디케이트가 처음으로 거짓이 되는 지점까지 발견된 요소를 버리고 거짓이 된 지점에서 작업을 중단 후 남은 모든 요소를 반환한다

dropWhile은 무한한 남은 요소를 가진 무한 스트림에서도 동작한다
````

#### 스트림 축소
스트림은 주어진 값 이하의 크기를 갖는 새로운 스트림을 반환하는 limit(n) 메서드를 지원한다

스트림이 정렬되어 있으면 최대 요소 n개를 반환할 수 있다
```` java
300칼로리 이상의 세 요리를 선택해서 만든 리스트

List<Dish> dishes = specialMenu.stream()
                               .filter(dish -> dish.getCalories() > 300)
                               .limit(3)
                               .collect(toList());

filter와 limit을 조합해서 사용한다

프레디케이트와 일치하는 처음 세 요소를 선택한 다음에 즉시 결과를 반환한다

정렬되지 않은 스트림에도 limit을 사용할 수 있으며 결과도 정렬이 안된 상태로 반환된다
````

#### 요소 건너뛰기
* 스트림은 처음 n개 요소를 제외한 스트림을 반환하는 skip(n) 메서드를 지원한다
  * n개 이하의 요소를 포함하는 스트림에 skip(n)을 호출하면 빈 스트림이 반환됨
  * limit(n)과 skip(n)은 상호 보완적인 연산을 수행함

```` java
300칼로리 이상의 처음 두 요리를 건너뛰고 300칼로리가 넘는 나머지 요리를 반환함

List<Dish> dishes = menu.stream()
                        .filter(d -> d.getCalories() > 300)
                        .skip(2)
                        .collect(toList());
````

### 매핑
특정 객체에서 특정 데이터를 선택하는 작업은 데이터 처리 과정에서 자주 수행되며 스트림의 map과 flatMap 메서드는 특정 데이터 선택 기능을 제공한다

#### 스트림의 각 요소에 함수 적용하기
* 스트림은 함수를 인수로 받는 map 메서드를 지원한다
* 인수로 제공된 함수는 각 요소에 적용되며 함수를 적용한 결과가 새로운 요소로 매핑된다
  * 이 과정은 기존의 값을 고친다는 개념 보다는 새로운 버전을 만드는 개념에 가깝다
    * 따라서 변환에 가까운 매핑이라는 단언를 사용한다
```` java
Dish::getName을 map 메서드로 전달해서 스트림의 요리명을 추출

List<String> dishNames = menu.stream()
                             .map(Dish::getName)
                             .collect(toList());
                             
getName은 문자열을 반환하기 때문에 map 메서드의 출력 스트림은 Stream<String> 형식을 가지게 된다

단어 리스트가 주어졌을때 각 단어가 포함하는 글자수의 리스트를 반환

List<String> wordList = Arrays.asList("test1", "test22", "test333");

List<String> wordLengList = wordList.stream()
                                    .map(String::length)
                                    .collect(toList());

map을 이용해 리스트의 각 요소에 함수를 적용한다
(각 요소에 적용할 함수는 단어를 인수로 받아서 길이를 반환해야됨)
String::length 메서드 참조를 이용해서 해결


그럼 각 요리명의 길이를 알려면 어떻게 해야될까?

List<String> dishNameLengths = menu.stream()
                             .map(Dish::getName)
                             .map(String::length)
                             .collect(toList());

map을 하나 더 연결해서 해결하면 된다
````

#### 스트림 평면화
앞에서 map을 이용해서 리스트의 단어, 길이 등을 확인하는 방법을 알아봤다

이걸 응용해서 리스트에서 고유 문자로 이루어진 리스트를 반환하는걸 알아보자
(["Hello", "World"] 이런 리스트를 ["H", "e," l", "l", "o", "W", "r", "d"] 로 바꾸는거)

```` java
일반적으로 시도 할만한 잘못된 해결책 1

words.stream()
     .map(word -> word.split(""))
     .distinct()
     .collect(toList());

위와 같은 코드로 해결이 될 것 같지만 map으로 전달한 람다는 각 단어의 String[]을 반환하게 된다
결국 최종 반환값은 Stream<String[]> 형태의 결과가 나오게 되는데 요구사항은 Stream<String> 형태이다

map과 Arrays.stream 을 사용한 잘못된 해결책 2

String[] arrayOfWords = {"Good", "Hello"};
Stream<String> streamOfwords = Arrays.stream(arrayOfwords);

배열이 문제라고 했으니 문자열을 받아 스트림을 만든 후 다시 시도해보자

words.stream()
     .map(word -> word.split(""))   // 각 단어를 개별 문자열 배열로 반환한다
     .map(Arrays::stream)           // 각 배열을 별도의 스트림으로 만든다
     .distinct()
     .collect(toList());

결국 위 방식도 List<Stream<String>> 형태로 반환값이 나타나며 요구사항을 만족하지 못한다

요구사항을 만족하려면 각 단어를 개별 문자열로 이루어진 배열로 만든 다음 각 배열을 별도의 스트림으로 만들어야 한다

flatMap이라는 메서드를 활용하면 이 문제를 해결 가능하다

flatMap을 사용하여 해결

List<String> uniqueCharacters = words.stream()
                                     .map(word -> word.split("")) // 각 단어를 개별 문자를 포함하는 배열로 반환한다
                                     .flatMap(Arrays::stream)     //생성된 스트림을 하나의 스트림으로 평면화 한다
                                     .distinct()
                                     .collect(toList());
                                     
flatMap은 각 배열을 스트림이 아닌 스트림의 콘텐츠로 맵핑해 map(Arrays::stream))과 다르게 하나의 평면화된 스트림을 반환한다

즉 flatMap 메서드는 스트림의 각 값을 다른 스트림으로 만든 후 모든 스트림을 하나의 스트림으로 연결하는 기능을 수행하게 된다
````
### 검색과 매칭
특정 속성이 데이터 집합에 있는지 확인하는 방법을 알아보자

#### anyMatch : 프레디케이트가 적어도 한 요소와 일치하는지 확인
```` java
menu에 채식요리가 있는지 확인

if(menu.stream().anyMatch(Dish::isVegetarian)) {
  System.out.println("채식 요리가 있음");
}

anyMatch는 불리언을 반환해서 최종 연산이 된다
````
#### allMatch : 프레디케이트가 모든 요소와 일치하는지 검사
anyMatch와 다르게 스트림의 모든 요소가 주어진 프레디케이트와 일치하는지 검사한다

```` java
메뉴가 건강식(모든 요리가 1000칼로리 이하)인지 확인

boolean isHealthy = menu.stream()
                        .allMatch(dish -> dish.getCalories() < 1000);
````

#### noneMatch :  주어진 프레디케이트와 일치하는 요소가 없는지 확인
allMatch와 반대 연산을 수행한다

```` java
메뉴가 건강식(모든 요리가 1000칼로리 이하)인지 확인

boolean isHealthy = menu.stream()
                        .noneMatch(d -> d.getCalories() >= 1000);
````
위에서 소개한 세가지 메서드는 스트림 쇼트서킷 기법을 활용한다
(자바의 && 이나 || 같은 연산)

#### 쇼트서킷
* and 연산이 많은 커다란 불리언 표현식을 평가할때 표현식에서 하나라도 거짓이 나오면 전체의 결과도 거짓이 되는 상황
* allMatch, noneMatch, findFirst, findAny 등... 이러한 연산은 모든 스트림의 요소를 처리하지 않고도 결과를 반환한다
  * 원하는 요소를 찾으면 바로 결과를 반환 가능
* limit도 쇼트서킷 연산이다 
  * 스트림의 모든 요소를 처리할 필요 없이 주어진 크기의 스트림을 생성하기 때문
* 전체 스트림을 처리하지 않아도 결과를 반환하고 싶은 경우 사용
* 무한한 요소를 가진 스트림을 유한한 크기로 줄일 수 있다

#### findAny : 현재 스트림에서 임의의 요소를 반환한다
요소검색에 사용하며 findAny 메서드를 다른 스트림 연산과 연결해서도 사용 가능하다

```` java
Optional<Dish> dish = menu.stream()
                          .filter(Dish::isVegetarian)
                          .findAny();
                          
스트림 파이프라인은 내부적으로 단일 과정으로 실행할 수 있도록 최적화된다

Optional: 값의 존재나 부재 여부를 표현하는 컨테이너 클래스이며 값이 없는 경우 어떤식으로 처리할지 강제하는 기능을 제공한다
(findAny는 아무 요소도 반환하지 않을 수 있기 때문에 Optional을 사용함)

//null 검사도 필요 없다
Optional<Dish> dish = menu.stream()
                          .filter(Dish::isVegetarian)
                          .findAny()
                          .ifPresent(dish -> System.out.println(dish.getName());  //값이 있으면 출력되고 없으면 아무 동작이 없다
          
````

#### findFirst : 첫 번째 요소 찾기
리스트나 정렬된 연속 데이터로 만들어진 스트림은 논리적인 아이템 순서가 있을 수도 있는데 이럴때 첫번째 요소를 찾을때 사용된다

#### findFirst 와 findAny는 언제 사용하나
병렬 실행에서는 첫 번째 요소를 찾기가 어려워 요소의 반환순서가 상관없다면 병렬 스트림에서는 제약이 적은 findAny를 사용한다

### 리듀싱
모든 스트림 요소를 처리해서 값으로 도출하는것(함수형 프로그래밍에선 폴드라고 부른다)

#### 요소의 합
```` java
for-each 루프를 이용해 리스트의 숫자를 더하는 코드

int snum = 0;
for (int x : numbers) {
  sum += x;
}

numbers의 각 요소는 결과에 반복적으로 더해진다
리스트에서 하나의 숫자가 남을 때까지 reduce 과정을 반복한다

코드에는 파라미터 두 개 사용

- sum 변수의 초기값 0
- 리스트의 모든 요소를 조합하는 연산(+)

위 코드에서 모든 숫자를 곱하는 연산을 구현하려면 복붙해서 조금 바꿔야 할 것이다
그렇게 하지 않고 reduce를 이용해 반복된 패턴을 추상화 가능하다

reduce를 이용해서 스트림의 모든 요소 더하는 코드

int sum = numbers.stream().reduce(0, (a, b) -> a + b);

reduce는 두 개의 인수를 가진다

- 초기값 0
- 두 요소를 조합해서 새로운 값을 만드는 BinaryOperator<T>, 여기선 람다로 (a,b) -> a + b 사용

이제 숫자를 곱하는 연산으로 바꾸는 경우 reduce로 다른 람다를 던져주면 쉽게 변경 가능하다

int product = numbers.stream().reduce(1, (a, b) -> a * b);

더하기 같은 경우 Integer에는 sum 메서드가 있으니 메서드 참조를 사용하면 조금 더 간편해진다

int sum = numbers.stream().reduce(0, Integer::sum);

초기값이 없는 reduce도 있는데 합계가 없음을 나타낼 수 있도록 Optional 객체를 반환한다

Optional<Integer> sum = numbers.stream().reduce((a, b) -> (a + b));

````
#### 최댓값과 최솟값
```` java
reduce를 이용해서 스트림에서 최댓값과 최솟값을 찾을 수 있다

reduce는 두 인수를 받는다

- 초깃값
- 스트림의 두 요소를 합쳐서 하나의 값으로 만드는데 사용할 람다

Optional<Integer> max = numbers.stream().reduce(Integer::max);  //min을 던져주면 최솟값

reduce 연산은 새로운 값을 이용해 스트림의 모든 요소를 소비할 때까지 람다를 반복 수행하면서 최댓값을 생산한다

````

#### reduce 메서드의 장점과 병렬화
* reduce를 쓰면 내부 반복이 추상화 되면서 내부 구현에서 병렬로 reduce를 실행 가능하다
  * 반복적인 합계에서는 sum 변수를 공유 하기 때문에 병렬화 하기가 어렵다
  * 강제적으로 동기화해도 병렬화로 얻어야 할 이득이 스레드 간 소모적인 경쟁 때문에 상쇄되어 버린다
* 중요한건 가변 누적자 패턴은 병렬화와 거리가 너무 먼 기법이다

