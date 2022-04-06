Chapter 04 스트림 소개
==================


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

