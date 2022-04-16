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



단어 리스트가 주어졌을 때 각 단어가 포함하는 글자 수의 리스트를 반환한다고 가정하자. 

리스트의 각 요소에 함수를 적용해야 한다. 이전 예제에서 확인했던 것처럼 map을 이용할 수 있다. 

각 요소에 적용할 함수는 단어를 인수로 받아서 길이를 반환해야 한다. 

결론적으로 다음처럼 메서드 참조 String::length를 map에 전달해서 문제를 해결할 수 있다.
``` java
List<String> words = Arrays.asList("Modern", "Java", "In", "Action");
List<Integer> wordLengths = words.stream()
                                 .map(String::length)
                                 .collect(toList());
```
각 요리명을 알고 싶다면 이렇게 하자. 
``` java
List<String> dishNames = menu.stream()
                             .map(dish::getName)
                             .map(String::length)
                             .collect(toList());
```
<hr>

### 5.3.2. 스트림 평면화

메서드 map을 이용해서 리스트의 각 단어의 길이를 반환하는 방법을 확인했다. 

리스트에서 고유 문자로 이루어진 리스트를 반환해보자. 

예를 들어 ["Hello", "World"] 리스트가 있다면 결과로 ["H", "e", "l", "o","W", "r", "d"]를 포함하는 리스트가 반환되어야 한다. 

리스트에 있는 각 단어를 문자로 매핑한 다음에 distinct로 중복된 문자를 필터링해서 쉽게 문제를 해결할 수 있을 것이라고 추측한 독자도 있을 것이다. 

다음처럼 문제를 해결할 수 있다. 

``` java
words.stream()
     .map(word -> word.split(""))
     .distinct()
     .collect(toList());
```
위 코드에서 map으로 전달한 람다는 각 단어의 String[](문자열 배열)을 반환한다는 점이 문제다. 

따라서 map 메소드가 반환한 스트림의 형식은 Stream<String[]>이다. 

우리가 원하는 것은 문자열의 스트림을 표현할 Stream<String>이다. 

### map과 Arrays.stream 활용

우선 배열 스트림 대신 문자열 스트림이 필요하다. 

문자열을 받아 스트림으로 만드는 Arrays.stream() 메서드가 있다. 

``` java
String[] arrayOfWords = {"Goodby", "World"};
Stream<String> streamOfWords = Arrays.stream(arrayOfWords);
```
Arrays.stream()을 적용해보자
``` java
words.stream()
     .map(word -> word.split(""))   // 각 단어를 개별 문자열 배열로 변환
     .map(Arrays::stream)  // 각 배열을 별도의 스트림으로 생성
     .distinct()
     .collect(toList());
```
결국 스트림 리스트(List<Stream<String>>)가 만들어지면서 문제가 해결되지 않았다. 

문제를 해결하려면 먼저 각 단어를 개별 문자열로 이루어진 배열로 만든 드ㅏ음에 각 배열을 별도의 스트림으로 만들어야 한다. 

### flatMap 사용

flatMap으로 해결 가능하다. 

``` java
List<String> uniqueCharacters = words.stream()
                                     .map(word -> word.split(""))    // 각 단어를 개별 문자열 배열로 변환
                                     .flatMap(Arrays::stream) // 생성된 스트림을 하나의 스트림을 평면화
                                     .distinct()
                                     .collect(toList());
```
flatMap은 각 배열을 스트림이 아니라 스트림의 콘텐츠로 매핑한다. 

즉, map(Arrays::stream)과 달리 flatMap은 하나의 평면화된 스트림을 반환한다. 

요약하면 flatMap 메서드는 스트림의 각 값을 다른 스트림으로 만든 다음에 모든 스트림을 하나의 스트림으로 연결하는 기능을 수행한다. 


### 퀴즈 5-2 매핑
1. 숫자리스트가 주어졌을 때 각 숫자의 제곱근으로 이루어진 리스트를 반환하시오. 예를 들어 [1,2,3,4,5]가 주어지면 [1,4,9,16,25]

* 정답 
숫자를 인수로 받아 제곱근을 반환하는 람다를 map에 넘겨주는 방법으로 문제를 해결할 수 있다. 

``` java
List<Integer> numbers = Arrays.asList(1,2,3,4,5);
List<Integer> squares = numbers.stream()
                               .map( n -> n * n)
                               .collect(toList());
```

2. 두 개의 숫자 리스트가 있을 때 모든 숫자 쌍의 리스트를 반환하시오. 예를 들어 두 개의 리스트 [1,2,3]과 [3,4] 가 주어지면 
[(1,3), (1,4), (2,3), (2,4), (3,3), (3,4)] 를 반환해야 한다. 

* 정답 
두 개의 map을 이용해서 두 리스트를 반복한 다음에 숫자 쌍을 만들 수 있다. 하지만 결과로 

Stream<Stream<Integer[]>>가 반환된다. 따라서 결과를 Stream<Integer[]>로 평면화한 스트림이 필요하다. 

flatMap을 사용해야 한다. 

``` java
List<Integer> numbers1 = Arrays.asList(1,2,3);
List<Integer> numbers2 = Arrays.asList(3, 4);
List<int[]> pairs = numbers1.stream()
                            .flatMap(i -> numbers2.stream()
                                                  .map(j -> new int[]{i, j})
                            )
                            .collect(toList());

```
3. 이전 예제에서 합이 3으로 나누어 떨어지는 쌍만 반환하려면 어떻게 해야 할까? 

filter를 프레디케이트와 함께 사용하면 스트림의 요소를 필터링할 수 있다. 
``` java
List<int[]> pairs = numbers1.stream()
            .flatMap(i -> numbers2.stream()
                    .filter(j -> (i+j) % 3 == 0)
                    .map(j-> new int[]{i, j}))
            .collect(Collectors.toList());
```

## 5.4 검색과 매칭

### 5.4.1 프레디케이트가 적어도 한 요소와 일치하는지 확인 
프레디케이트가 주어진 스트림에서 적어도 한 요소와 일치하는지 확인할 때 anyMatch 메서드를 이용한다. 

예를 들어 다음 코드는 menu에 채식요리가 있는지 확인하는 예제다. 

``` java
if (menu.stream().anyMatch(Dish::isVegetarian)) {
    System.out.println("The menu is (somewhat) vegetarian friendly!!");
}
```
anyMatch는 불리언을 반환하므로 최종 연산이다. 

### 5.4.2 프레디케이트가 모든 요소와 일치하는지 검사 
allMatch 메서드는 anyMatch와 달리 스트림의 모든 요소가 주어진 프레디케이트와 일치하는지 검사한다. 

예를 들어 메뉴가 건강식(모든 요리가 1000칼로리 이하면 건강식으로 간주)인지 확인할 수 있다.
``` java
boolean isHealthy = menu.stream()
                        .allMatch(dish -> dish.getCalories() < 1000);
```

### NONEMATCH
noneMatch는 allMatch와 반대 연산을 수행한다. 즉, noneMatch는 주어진 프레디케이트와 일치하는 요소가 없는지 확인한다.

``` java
boolean isHealthy = menu.stream()
                        .noneMatch(dish -> dish.getCalories() >= 1000);
```


>anyMatch, allMatch, noneMatch 세 메서드는 스트림 쇼트서킷 기법, 즉 자바의 &&, || 와 같은 연산을 활용한다. 

### 쇼트서킷 평가
때로는 전체 스트림을 처리하지 않더라도 결과를 반환할 수 있다. 예를 들어 여러 and 연산으로 연결된 커다란 불리언 표현식을 표현한다고 가정하자. 

표현식에서 하나라도 거짓이라는 결과가 나오면 나머지 표현식의 결과와 상관없이 전체 결과도 거짓이 된다. 

이런 상황을 **쇼트 서킷**이라고 부른다. 

allMatch, noneMatch, findFirst, findAny 등의 연산은 모든 스트림의 요소를 처리하지 않고도 결과를 반환할 수 있다. 

원하는 요소를 찾았으면 즉시 결과를 반환할 수 있다. 마찬가지로 스트림의 모든 요소를 처리할 필요 없이 주어진 크기의 스트림을 생성하는 limit도 쇼토서킷 연산이다. 

특히 무한한 요소를 가진 스트림을 유한한 크기로 줄일 수 있는 유용한 연산이다. 




## 5.4.3 요소 검색

findAny 메서드는 현재 스트림에서 임의의 요소를 반환한다. findAny 메서드를 다른 스트림 연산과 연결해서 사용할 수 있다. 

예를 들어 다음 코드처럼 filter와 findAny를 이용해서 채식요리를 선택할 수 있다. 

``` java
Optional<Dish> dish = menu.stream()
                          .filter(Dish::isVegetarian)
                          .findAny();
```
스트림 파이프라인은 내부적으로 단일 과정으로 실행할 수 있도록 최적화된다. 

즉, 쇼트서킷을 이용해서 결과를 찾는 즉시 실행을 종료한다. 

### Optional이란 무엇인가?

Optional<T>  클래스(java.util.Optional) 는 값의 존재나 부재 여부를 표현하는 컨테이너 클래스이다. 

이전 예제에서 findAny는 아무 요소도 반환하지 않을 수 있다. 

null은 쉽게 에러를 일으킬 수 있으므로 자바8 라이브러리 설계자는 Optional<T>를 만들었다. 

Optional은 값이 존재하는지 확인하고 값이 없을 때 어떻게 처리할지 강제하는 기능을 제공한다.

* isPresent()는 Optional이 값을 포함하면 참(true)을 반환하고, 값을 포함하지 않으면 거짓(false)을 반환한다.
* ifPresent(Consumer<T> block)은 값이 있으면 주어진 블록을 실행한다. Consumer 함수형 인터페이스에는 T형식의 인수를 받으며 void를 반환하는 람다를 전달할 수 있다. 
* T get()은 값이 존재하면 값을 반환하고, 값이 없으면 NoSuchElmentException을 일으킨다.
* T orElse (T other)는 값이 있으면 값을 반환하고, 값이 없으면 기본값을 반환한다.


### 5.4.4 첫 번째 요소 찾기

숫자 리스트에서 3으로 나누어 떨어지는 첫 번째 제곱값을 반환한느 다음 코드를 살펴보자. 

``` java
  List<Integer> someNumbers = Arrays.asList(1, 2, 3, 4, 5);
        Optional<Integer> firstSquareDivisibleByThree = someNumbers.stream()
                .map(n -> n* n)
                .filter(n -> n % 3 == 0)
                .findFirst();  // 9
```

## 5.5 리듀싱

리듀싱 연산 : 모든 스트림 요소를 처리해서 값으로 도출하는 연산을 말한다. 


### 5.5.1 요소의 합

``` java
int sum = 0;
for (int x : numbers) {
    sum += x;
}
```

``` java
int sum = numbers.stream().reduce(0, (a,b) -> a+ b);
```
reduce는 두 개의 인수를 갖는다
* 초깃값()
* 두 요소를 조합해서 새로운 값을 만드는 BinaryOperator<T> 예제에서는 람다 표현식 (a, b) -> a+ b를 사용했다. 

reduce로 다른 람다, 즉 (a, b) -> a * b를 넘겨주면 모든 요소에 곱셈을 적용할 수 있다. 

``` java
int product = numbers.stream().reduce(1, (a,b) -> a * b);
```


### 초깃값 없음

초깃값을 받지 않도록 오버로드된 reduce도 있다. 그러나 이 reduce는 Optional 객체를 반환한다. 

``` java
Optional<Integer> sum = numbers.stream().reduce((a,b) -> (a+b));
```
스트림에 아무 요소도 없는 상황이면 초깃값이 없으므로 reduce는 합계를 반환할 수 없다. 
따라서 합계가 없음을 가리킬 수 있도록 Optional 객체로 감싼 결과를 반환한 것이다. 

### 5.5.2 최댓값과 최솟값

reduce를 이용해서 스트림의 최댓값을 찾는다. 
``` java
Optional<Integer> max = numbers.stream().reduce(Integer::max);
```

Integer.min으로 최솟값 찾는다. 
``` java
Optional<Integer> min = numbers.stream().reduce(Integer::min);
```

#### 퀴즈 5-3 리듀스
map과 reduce를 이용해서 스트림의 요리 개수를 계산하시오. 

* 정답 
스트림의 각 요소를 1로 매핑한 다음에 reduce로 이들의 합계를 계산하는 방식으로 문제를 해결할 수 있다. 
즉, 스트림에 저장된 숫자를 차례로 더한다. 

``` java
int count = menu.stream()
                .map(d -> 1)
                .reduce(0, (a,b) -> a+ b);
```
map과 reduce를 연결하는 기법을 맵 리듀스 패턴이라 하면 쉽게 병렬화하는 특징 덕분에 
구글이 웹 검색에 적용하면서 유명해졌다. 

``` java
long count = menu.stream().count();
```

### 5.7 숫자형 스트림

reduce  메서드로 스트림 요소의 합을 구하는 예제를 살펴봤다. 

예를 들어 다음처럼 메뉴의 칼로리 합계를 계산할 수 있다. 

``` java
int calories = menu.stream()
                    .map(Dish::getCalories)
                    .reduce(0, Integer::sum);
```
사실 위 코드에는 박싱 비용이 숨어 있다. 내부적으로 합계를 계산하기 전에 Integer를 기본형으로 언박싱해야 한다. 

``` java
int calories = menu.stream()
                    .map(Dish::getCalories)
                    .sum();
```
하지만 위 코드처럼 sum메서드를 직접 호출할 수 없다. 

map 메서드가 Stream<T>를 생성하기 때문이다. 

스트림의 요소 형식은 Integer지만 인터페이스에는 sum메서드가 없다. 

왜 sum메서드가 없을까? 예를 들어 menu처럼 Stream<Dish> 형식의 요소만 있다면 sum이라는 연산을 수정할 수 없기 때문이다. 

다행히도 스트림 API 숫자 스트림을 효율적으로 처리할 수 있도록 **기본형 특화 스트림**을 제공한다. 


### 5.7.1 기본형 특화 스트림
자바 8에서는 세 가지 기본형 특화 스트림을 제공한다. 

스트림 API는 박싱 비용을 피할 수 있도록 'int요소에 특화된 IntStream', 

'double요소에 특화된 DoubleStream', 'long요소에 특화된 LongStream'을 제공한다. 

각각의 인터페이스는 숫자 스트림의 합계를 계산하는 sum,  최댓값 요소를 검색하는 max 같이 자주 사용하는 숫자 관련 리뉴싱 연산 수행 메서드를 제공한다. 

또한 필요할 때 다시 객체 스트림으로 복원하는 기능도 제공한다. 

특화 스트림은 오직 박싱 과정에서 일어나는 효율성과 관련 있으며 스트림에 추가 기능을 제공하지 않은 사실을 기억하자. 


#### 숫자 스트림으로 매핑 
스트림을 특화 스트림으로 변환할 때는 mapToInt, mapToDouble, mapToLong 세 가지 메서드를 가장 많이 사용한다. 

이들 메서드는 map과 정확히 같은 기능을 수행하지만, Stream<T> 대신 특화된 스트림을 반환한다. 

``` java
int calories = menu.stream() // Stream<Dish> 반환
                   .mapToInt(Dish::getCalories) // IntStream 반환
                   .sum();
```
mapToInt 메서드는 각 요리에서 모든 칼로리(Integer 형식)를 추출한 다음에 IntStream을 (Stream<Integer>가 아님) 반환한다.

따라서 IntStream 인터페이스에서 제공하는 sum 메서드를 이용해서 칼로리 합계를 계산할 수 있다. 

스트림이 비어 있으면 sum은 기본값 0을 반환한다. 


<hr>

### 객체 스트림으로 복원하기 
숫자 스트림을 만든 다음에, 원상태인 특화되지 않은 스트림으로 복원할 수 있을까? 

예를 들어 IntStream은 기본형의 정수값만 만들 수 있다. IntStream의 map 연산은 'int를 인수로 받아서 int를 반환하는 람다'

하지만 정수가 아닌 Dish 같은 다른 값을 반환하고 싶으면 어떻게 해야할까?

그러려면 스트림 인터페이스에 정의된 일반적인 연산을 사용해야 한다.

다음 예제처럼 boxed메서드를 이용해서 특화 스트림을 일반 스트림으로 변환할 수 있다. 

``` java
IntStream intStream = menu.stream().mapToInt(Dish::getCalories);
StreamL<Integer> stream = intStream.boxed()
```

### 기본값 OptionalInt
합계 예제에서는 0이라는 기본값이 있었으므로 별 문제가 없었다. 

하지만 IntStream에서 최댓값을 찾을 때는 0이라는 기본값 때문에 잘못된 결과가 도출될 수 있다. 

스트림에 요소가 없는 상황과 실제 최댓값이 0인 상황을 어떻게 구별할 수 있을까?

OptionalInt, OptionalDouble, OptionalLong 세 가지 기본형 특화 스트림 버전도 제공한다. 

다음처럼 OptionalInt를 이용해서 IntStream의 최댓값 요소를 찾을 수 있다. 
``` java
OptionalInt maxCalories = menu.stream()
                              .mapToInt(Dish::getCalories)
                              .max();
```

이제 OptionalInt를 이용해서 최댓값이 없는 상황에 사용할 기본값을 명시적으로 정의할 수 있다. 
``` java
int max = maxCalories.orElse(1); // 값이 없을 때 기본 최댓값을 명시적으로 설정
```

### 5.7.2 숫자 범위 
1에서 100 사이의 숫자를 생성하려 한다고 가정하자. 

자바 8의 IntStream과 LongStream에서는 range와 rangeClosed라는 두 가지 정적 메서드를 제공한다. 

두 메서드 모두 첫 번째 인수로 시작값을, 두 번째 인수로 종료값을 갖는다. 

range메서드는 시작값과 종료값이 결과에 포함되지 않는 반면 rangeClosed는 시작값과 종료값이 결과에 포함된다는 점이 다르다. 

``` java
IntStream evenNumbers = IntStream.rangeClosed(1, 100) // 1~100의 범위를 나타낸다. 
                                 .filter(n -> n % 2 == 0) // 1~100까지의 짝수 스트림
System.out.println(evenNumbers.count()); // 1~100까지에는 50개의 짝수가 있다. 
```

## 5.7.3 숫자 스트림 활용 : 피타고라스 수

### 피타고라스 수
피타고라스 수가 무엇인지 기억하고 있는가? 

피타고라스는 a * a + b * b = c * c 공식을 만족하는 세 개의 정수(a,b,c)가 존재함이 발견했다. 

피타고라스 수를 그림으로 그리면 직각 삼각형이 만들어진다. 

### 세 수 표현하기 

### 좋은 필터링 조합 
``` java
filter(b -> Math.sqrt(a*a + b*b) % 1 == 0)
```

### 집합 생성
``` java
stream.filter(b -> Math.sqrt(a*a + b*b) % 1 == 0)
      .map( b -> new int[]{a, b, (int) Math.sqrt(a*a + b*b)});
```

### b값 생성 

``` java
IntStream.rangeClose(1, 100)
         .filter(b-> Math.sqrt(a*a + b*b) % 1 == 0)
         .boxed() 
         .map(b -> new int[]{a, b, (int) Math.sqrt(a*a + b*b)});
```
filter연산 다음에 rangeClosed가 반환한 IntStream을 boxed를 이용해서 Stream<Integer>로 복원했다. 

map은 스트림의 각 요소를 int 배열로 변환하기 때문이다. 

개체값 스트림을 반환하는 IntStream의 mapToObj 메서드를 이용해서 이 코드를 재구현 할 수 있다. 

``` java
IntStream.rangeClose(1, 100)
         .filter(b-> Math.sqrt(a*a + b*b) % 1 == 0)
         .mapToObj(b -> new int[]{a, b, (int) Math.sqrt(a*a + b*b)});
```

### a값 생성

``` java
Stream<int[]> pythagoreanTriples = 
    IntStream.rangeClosed(1, 100).boxed()
             .flatMap( a -> 
              IntStream.rangeClosed(a, 100) 
                       .filter(b-> Math.sqrt(a*a + b*b) % 1 == 0)
                       .mapToObj(b -> new int[]{a, b, (int) Math.sqrt(a*a + b*b)})
                       );
```

### 코드 개선 
``` java
Stream<int[]> pythagoreanTriples = 
    IntStream.rangeClosed(1, 100).boxed()
             .flatMap( a -> 
              IntStream.rangeClosed(a, 100) 
                       .mapToObj(b -> new int[]{a, b, (int) Math.sqrt(a*a + b*b)})
                       .filter( t-> t[2] % 1 == 0)
                       );
```


## 5.8 스트림 만들기 
### 5.8.1 값으로 스트림 만들기 
임의의 수를 인수로 받는 정적 메서드 Stream.of를 이용해서 스트림을 만들 수 있다. 


``` java
Stream<String> stream = Stream.of("Modern ", "Java ", "In ", "Action");
stream.map(String::toUpperCase).forEach(System.out::println);
```

``` java
Stream<String> emptyStream = Stream.empty()
```
### 5.8.2 null이 될 수 있는 객체로 스트림 만들기 



### 5.8.3 배열로 스트림 만들기 
배열을 인수로 받는 정적 메서드 Arrays.stream을 이용해서 스트림을 만들 수 있다. 

예를 들어 다음처럼 기본형 int로 이루어진 배열을 IntStream으로 변환할 수 있다. 
``` java
int[] numbers = {2, 3, 5, 7, 11, 13};
int sum = Arrays.stream(numbers).sum();
```