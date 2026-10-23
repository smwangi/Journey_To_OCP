package Streams;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
public class ReduceExamples {

    List<String> namses = List.of("Alice", "Bob", "Charlie", "David", "Eve", "Frank", "Grace", "Alice", "Bob", "Charlie");
    Optional<String> longestName = namses.stream()
            .reduce((name1, name2) -> name1.length() > name2.length() ? name1 : name2);

    List<Double> prices = List.of(19.99, 29.99, 9.99, 49.99, 15.99);
    Optional<Double> totalPrice = prices.stream()
            .reduce(Double::sum);
    double totalPriceValue = prices.stream()
            .reduce(0.0, Double::sum); // Using reduce with an identity value

    // Example of using reduce to find the maximum value
    Optional<Double> maxPrice = prices.stream()
            .reduce(Double::max);

    // Example of using reduce to find the minimum value
    Optional<Double> minPrice = prices.stream()
            .reduce(Double::min);

    // Combining objects with reduce
    List<Person> people = List.of(
            new Person("Alice", 30),
            new Person("Bob", 25),
            new Person("Charlie", 35)
    );

    Optional<Person> oldestPerson = people.stream()
            .reduce((p1, p2) -> p1.getAge() > p2.getAge() ? p1 : p2);
    
    int adults = people.stream()
            .map(person -> person.getAge() >= 18 ? 1 : 0)
            .reduce(0, (a, b) -> a + b);
    
    // Map reduce with dfferent types
    // Count the total length of all strings
    int totalLength = namses.stream()
        .reduce(0, (sum, str) -> sum + str.length(), Integer::sum);

    String joined = namses.parallelStream()
        .reduce(
            "",
            (partial, item) -> partial.isEmpty() ? item : partial + ", "+ item, //accumulator builds the string`
            (s1, s2) -> s1.isEmpty() ? s2 : (s2.isEmpty() ? s1 : s1 + ", " + s2)); // combiner merges two partial results

    // Map-Reduce pattern Frequency count
    Map<String, Integer> frequencyCount = namses.parallelStream()
    .reduce(
        new HashMap<>(),
    (map, name) -> {
        map = new HashMap<>(map); // Create a new map to avoid modifying the original
        map.put(name, map.getOrDefault(name, 0) + 1);
        return map;
    },
    (map1, map2) -> {
        map2.forEach((key, value) -> map1.merge(key, value, Integer::sum));
        return map1;
    });

    public static void main(String[] args) {
        ReduceExamples examples = new ReduceExamples();
        examples.longestName.ifPresent(name -> System.out.println("Longest name: " + name));
        examples.totalPrice.ifPresent(price -> System.out.println("Total price: " + price));
        examples.maxPrice.ifPresent(max -> System.out.println("Maximum price: " + max));
        examples.minPrice.ifPresent(min -> System.out.println("Minimum price: " + min));
        System.out.println("Total price with identity: " + examples.totalPriceValue);
        examples.oldestPerson.ifPresent(person -> System.out.println("Oldest person: " + person.getName() + ", Age: " + person.getAge()));
        System.out.println("Number of adults: " + examples.adults);
        System.out.println(examples.totalLength);
        System.out.println("Joined names: " + examples.joined);
        System.out.println("Frequency Count: " + examples.frequencyCount);
    }  
}

class Person {
    private String name;
    private int age;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }
}
