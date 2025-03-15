package FunctionalProgramming.Streams;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * StreamSample
 * Streams provide a clean and concise way to perform operations on collections.
 * They support operations like filter, map, reduce, etc.
 */
public class StreamSample {
    public static void main(String[] args) {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        int numOfSquares = numbers.stream()
                .filter(n -> n % 2 == 0)
                .map(n -> n * n) // applies a function to each element of the stream (square of each element)
                .reduce(0, Integer::sum); // reduces the stream to a single value by summing all elements

        System.out.println("Sum of squares of even numbers: " + numOfSquares);

        // Combining Functional interface with Streams
        List<Person> persons = Arrays.asList(
                new Person("Alice", 23),
                new Person("Bob", 25),
                new Person("Charlie", 30),
                new Person("David", 35)
        );

        // Predicate to check if person is older than 25
        Predicate<Person> olderThan25 = person -> person.getAge() > 25; // defines a lambda that checks if a person is older than 25

        // Filter persons older than 25
        persons.stream()
                .filter(olderThan25)
                .forEach(person -> System.out.println(person.getName() + " is older than 25"));

        persons.stream()
                .filter(olderThan25)
                // sort by name
                .sorted(Comparator.comparing(Person::getName))
                .toList()
                .forEach(person -> System.out.println(person.getName() + " is older than 25 and sorted by name"));

        // Map, applies a function to each element and returns a new stream of the results
        List<String> words = Arrays.asList("apple", "banana", "cherry", "date", "elderberry");
        List<Integer> wordLengths = words.stream()
                .map(String::length)
                .toList();
        System.out.println("Word lengths: " + wordLengths);

        // FlatMap, flattens nested structures, mapping each element to a stream and concatenating the resulting streams
        List<List<String>> listOfLists = Arrays.asList(
                List.of("apple", "banana"),
                List.of("cherry", "date"),
                List.of("elderberry", "fig")
        );

        List<String> flatList = listOfLists.stream()
                .peek(System.out::println)
                .flatMap(List::stream)
                .toList();

        System.out.println(flatList);

        String[] wordArray = words.toArray(String[]::new);
        System.out.println(Arrays.toString(wordArray));

        // anyMatch, allMatch, noneMatch
        boolean anyMatch = words.stream().anyMatch(word -> word.startsWith("a"));
        boolean allMatch = words.stream().allMatch(word -> word.length() > 3);
        boolean noneMatch = words.stream().noneMatch(word -> word.endsWith("z"));
        System.out.println("Any match: " + anyMatch);
        System.out.println("All match: " + allMatch);
        System.out.println("None match: " + noneMatch);

        // findFirst, findAny
        Optional<String> first = words.stream().findFirst();
        Optional<String> any = words.stream().findAny();
        first.ifPresent(word -> System.out.println("First word: " + word));
        any.ifPresent(word -> System.out.println("Any word: " + word));

        // groupingBy length of String
        var groupedByLength = words.stream()
                .collect(Collectors.groupingBy(String::length));
        System.out.println(groupedByLength);

        // partitioningBy
        var partitionedBy = numbers.stream()
                .collect(Collectors.partitioningBy(n -> n % 2 == 0));
        System.out.println(partitionedBy);

        // joining
        String joined = String.join(", ", words);
        System.out.println(joined);

        // Summarizing int
        IntSummaryStatistics stats = numbers.stream()
                .collect(Collectors.summarizingInt(Integer::intValue));
        System.out.println(stats);

        // toMap
        Map<String, Integer> wordLengthToMap = words.stream()
                .collect(Collectors.toMap(word -> word, String::length));
        System.out.println(wordLengthToMap);

        // mapping
        List<Integer> wordsLength = words.stream().map(String::length).toList();
        System.out.println(wordsLength);

        // using multiple collectors
        Map<Integer, Long> groupedByLengthCount = words.stream()
                .collect(Collectors.groupingBy(String::length, Collectors.counting()));
        System.out.println(groupedByLengthCount);

        // Grouping by length and joining Strings
        Map<Integer, String> groupedByLengthJoining = words.stream()
                .collect(Collectors.groupingBy(String::length, Collectors.joining(", ")));
        System.out.println(groupedByLengthJoining);
    }
}
