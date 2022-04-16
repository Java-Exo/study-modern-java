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

### 숫자형 스트림

```` java
reduce 메서드로 스트림 요소의 합을 구하는 예제
- 메뉴의 칼로리 합계를 계산할 수 있다

int calories = menu.stream()
                    .map(Dish::getCalories)
                    .reduce(0, Integer::sum);
                    
위 코드에는 박싱 비용이 숨어있다
내부적으로 합계를 계산하기 전에 Integer를 기본형으로 언박싱 해야한다
아래처럼 sum을 직접 호출하면 더 좋았겠지만 map 메서드가 Stream<T>를 생성하기 때문에 sum 메서드가 없어서 불가능하다
int calories = menu.stream()
                    .map(Dish::getCalories)
                    .sum(); //map이 반환하는 객체에 sum이 없어서 실제로는 불가능한 코드
그렇다면 왜 sum이 없을까??
Stream<커맨드객체> 형식의 요소라면 sum이라는 연산을 수행 불가능 하기 때문이다
하지만 스트림 API는 숫자 스트림을 효율적으로 처리할 수 있도록 기본형 특화 스트림을 제공한다                    
````

#### 기본형 특화 스트림
* 자바에서는 세가지 기본형 특화 스트림을 제공한다
* 스트림 API는 박싱 비용을 피할 수 있도록 int 요소에 특화된 IntStream, double 요소에 특화된 DoubleStream, long 요소에 특화된 LongStream을 제공한다
* 각각의 인터페이스는 숫자 스트림의 합계를 계산하는 sum, 최댓값 요소를 검색하는 max 같이 자주 사용하는 숫자 관련 리듀싱 연산 수행 메서드를 제공한다
* 또한 필요할때 다시 객체 스트림으로 복원하는 기능도 제공한다
* 특화 스트림은 오직 박싱 과정에서 일어나는 효율성과 관련 있으며 스트림에 추가 기능을 제공하지는 않는다

숫자 스트림으로 매핑
* 스트림을 특화 스트림으로 변환할 때는 mapToInt, mapToDouble, mapToLong 세 가지 메서드를 가장 많이 사용한다
* 이들 메서드는 map과 정확히 같은 기능을 수행하지만 Stream<T>대신 특화된 스트림을 반환한다
```` java
int calories = menu.stream()                      //Stream<Dish> 반환
                    .mapToInt(Dish::getcalories)  //IntStream 반환
                    .sum();
````
* mapToInt 메서드는 각 요리에서 모든 칼로리(Integer 형식)를 추출한 다음에 IntStream을 반환한다
* 따라서 IntStream 인터페이스에서 제공하는 sum 메서드를 이용해서 칼로리 합계를 계산할 수 있다
* 스트림이 비어있으면 sum은 기본값 0을 반환한다
* IntStream은 max,min,average 등 다양한 유틸리티 메서드도 지원한다

객체스트림으로 복원하기
* 객체 스트림으로 복원하려면 스트림 인터페이스에 정의된 일반적인 연산을 사용해야 한다
```` java
IntStream intStream = menu.stream().mapToInt(Dish::getCalories);  //스트림을 숫자 스트림으로 변환
Stream<Integer> stream = intStream.boxed();                       //숫자 스트림을 스트림으로 변환

````

기본값 :  OptionalInt
* 기본값이 없을때 스트림에 요소가 없는 상황과 실제 반환값(최댓값 등..)이 0인 상황을 구별할 수 있다
* OptionalInt를 이용해서 반환값(최댓값)이 없는 상황에서 사용할 기본값을 명시적으로 지정 가능하다
```` java
OptionalInt를 이옹해서 IntStream의 최댓값 요소 찾기

OptionalInt maxCalories = menu.stream()
                               .mapToInt(Dish::getCalories)
                               .max();
OptionalInt를 이용해서 최댓값이 없는 상황에 사용할 기본값을 명시적으로 정의 가능
int max = maxCalories.orElse(1);  //값이 없을때 기본 최댓값을 명시적으로 설정
````
#### 숫자범위
* 프로그램에서는 특정 범위의 숫자를 이용해야 하는 상황이 자주 발생한다
```` java
1 에서 100 사이의 숫자를 생성하려면 자바 8의 IntStream과 LongStream에서는 range와 rangeClosed라는 두 가지 정적 메서드를 제공한다
두 메서드 모두 첫 번째 인수로 시작값을, 두 번째 인수로 종료값을 가진다
range 메서드는 시작값과 종료값이 결과에 포함되지 않는다
rangeClosed는 시작값과 종료값이 결과에 포함된다

IntStream evenNumbers = IntStream.rangeClosed(1, 100)       //1~100의 범위를 나타낸다
                                  .filter(n -> n % 2 == 0);
System.out.println(evenNumbers.count());                    //1부터 100까지에는 50개의 짝수가 있다

위 코드처럼 rangeClosed를 이용해서 1부터 100까지의 숫자를 만들 수 있다
rangeClosed의 결과는 스트림으로 filter 메서드를 이용해서 짝수만 필터링할 수 있다
filter를 호출해도 실제로는 아무 계산도 이루어지지 않는다
최종적으로 결과 스트림에 count를 호출한다
````

### 스트림 만들기
#### 값으로 스트림 만들기
* 임의의 수를 인수로 받는 정적 메서드 Stream.of 이용해서 스트림을 만들 수 있다
* 예를 들어 다음 코드는 Stream.of로 문자열 스트림을 만드는 예제다
* 스트림의 모든 문자열을 대문자로 변환한 후 문자열을 하나씩 출력한다
```` java
Stream<String> stream = Stream.of("Modern", "Java", "In", "Action");
stream.map(String::toUpperCase).forEach(System.out.println);

다음 처럼 empyty 메서드를 이용해서 스트림을 비울 수 있다
````

#### null이 될 수 있는 객체로 스트림 만들기
* 자바 9에서는 null이 될 수 있는 개체를 스트림으로 만들 수 있게 Stream.ofNullable을 이용해서 다음 처럼 구현 가능하다
```` java
명시적으로 null 확인

String homeValue = System.getProperty("home");
Stream<String> homeValueStream = homeValue == null ? Stream.empty() : Stream.of(value);


Stream.ofNullable을 이용해 구현 가능하다
Stream<String> homeValueStream = Stream.ofNullable(System.getProrerty("home"));

null이 될 수 있는 객체를 포함하는 스트림 값을 flatMap과 함께 사용하는 상황에서 좋은 패턴
Stream<String< values = Stream.of("config", "home", "user")
                              .flatMap(key -> Stream.ofNullable(System.getProperty(key))
````

#### 배열로 스트림 만들기
* 배열을 인수로 받는 정적 메서드 Arrays.stream을 이용해서 스트림을 만들 수 있다
```` java
기본형 int로 이루어진 배열을 IntStream으로 변환 가능
int[] numbers = {2, 3, 5, 7, 11, 13};
int sum = Arrays.stream(numbers).sum();
````

#### 파일로 스트림 만들기
* 파일을 처리하는 등의 I/O 연산에 사용하는 자바의 NIO API도 스트림을 활용할 수 있도록 업데이트 되었다
* java.nio.file.Files의 많은 정적 메서드가 스트림을 반환한다
```` java

//스트림은 자원을 자동으로 해지할 수 있는 AutoCloseable 이므로 try-finally가 필요 없다
//Charset.defaultCharset()))
long uniqueWords = 0;
try(Stream<String> lines = Files.lines(Paths.get("data.txt"), Charset.defaultCharset())) {
uniqueWords = lines.flatMap(line -> Arrays.stream(line.split(" ")))
                  .distinct()         // 중복 제거
                  .count();           //단어 스트림 생성
                  
Files.lines로 파일의 각 행 요소를 반환하는 스트림을 얻을 수 있다
스트림의 소스가 I/O 자원이므로 이 메소드를 try/catch 블록으로 감쌌고 메모리 누수를 막으려면 자원을 닫아야 한다
기존에는 finally 블록에서 자원을 닫았다
Stream 인터페이스는 AutoCloseable 인터페이스를 구현해야 한다
따라서 try 블록 내의 자원은 자동으로 관리된다
line에 split 메서드를 호출해서 각 행의 단어를 분리할 수 있다
각 행의 단어를 여러 스트림으로 만드는 것이 아니라 flatMap으로 스트림을 하나로 평면화했다
마지막으로 distinct와 count를 연결해서 스트림의 고유 단어 수를 계산한다
````
