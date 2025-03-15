package Streams;
// Streams are a sequence of elements supporting sequential and parallel aggregate operations.
// The following example demonstrates how to use streams to filter a list of strings and print them.
// The example uses the filter() method to filter the list of strings that start with the letter "a".
// The forEach() method is used to print the filtered list.
// The output of the example is:
// apple
// avocado
// The example uses the following steps:
// 1. Create a list of strings.
// 2. Convert the list to a stream.
// 3. Use the filter() method to filter the list of strings that start with the letter "a".
// 4. Use the forEach() method to print the filtered list.
// The following example demonstrates how to use streams to filter a list of strings and print them.
// The example uses the filter() method to filter the list of strings that start with the letter "a".
// The forEach() method is used to print the filtered list.
// The output of the example is:
// apple
// avocado
// The example uses the following steps:
// 1. Create a list of strings.
// 2. Convert the list to a stream.
// 3. Use the filter() method to filter the list of strings that start with the letter "a".
// 4. Use the forEach() method to print the filtered list.
// The following example demonstrates how to use streams to filter a list of strings and print them.
// The example uses the filter() method to filter the list of strings that start with the letter "a".
// The forEach() method is used to print the filtered list.
// The output of the example is:
// apple
// avocado
// The example uses the following steps:
// 1. Create a list of strings.
// 2. Convert the list to a stream.
// 3. Use the filter() method to filter the list of strings that start with the letter "a".
// 4. Use the forEach() method to print the filtered list.
// The following example demonstrates how to use streams to filter a list of strings and print them.
// The example uses the filter() method to filter the list of strings that start with the letter "a".
// The  forEach() method is used to print the filtered list.

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Streams {
    public static void main(String[] args) {
        List<Integer> list = Arrays.asList(1, 2, 3);
        Function<Integer, Integer> square = x -> x * x;
        final var min = list.stream().map(square).peek(System.out::println).min((a, b) -> a - b);

        List<Person> people = Arrays.asList(
                new Streams().new Person("John", 30),
                new Streams().new Person("Julie", 35),
                new Streams().new Person("Jane", 40),
                new Streams().new Person("Alex", 20),
                new Streams().new Person("Alice", 35),
                new Streams().new Person("Bob", 22),
                new Streams().new Person("Anna", 32),
                new Streams().new Person("Charlie", 28),
                new Streams().new Person("David", 40)
        );
        Predicate<Person> below35 = person -> person.getAge() < 35;
        Predicate<Person> nameStartsWithA = person -> person.getName().startsWith("A");
        Predicate<Person> combinedCriteria = below35.and(nameStartsWithA);
        Predicate<Person> olderThan30 = person -> person.getAge() > 30;
        Predicate<Person> nameStartsWithJ = person -> person.getName().startsWith("J");
        Predicate<Person> youngerThan25 = person -> person.getAge() < 25;
        Predicate<Person> combinedCriteria2 = olderThan30.and(nameStartsWithJ).or(youngerThan25);

        //1. And Operation
        List<Person> andFiltered = people.stream()
                .filter(olderThan30.and(nameStartsWithA))
                .toList();
        System.out.println("\n People older than 30 and name starts with A");
        andFiltered.forEach(x -> System.out.println(x.getName() +", AGE: "+x.getAge()));

        //2. Or Operation
        List<Person> orFiltered = people.stream()
                .filter(olderThan30.or(nameStartsWithJ))
                .toList();
        System.out.println("\n People older than 30 or name starts with J");
        orFiltered.forEach(x -> System.out.println(x.getName() +", AGE: "+x.getAge()));

        //3. Negate Operation
        List<Person> negateFiltered = people.stream()
                .filter(olderThan30.negate())
                .toList();
        System.out.println("\n People not older than 30");
        negateFiltered.forEach(x -> System.out.println(x.getName() +", AGE: "+x.getAge()));

        List<Person> filtered = people.stream()
                .filter(combinedCriteria)
                .toList();
        filtered.forEach(x -> System.out.println(x.getName()));

        Stream.generate(() -> 9 * 3)
                .limit(3)
                .forEach(System.out::println);
        Stream.iterate(1, x -> x + 1)
                .limit(5)
                .forEach(System.out::println);

        // Generate numbers starting from 1, increment by 2, but stop at 20
        Stream.iterate(1, n -> n <= 20, n -> n + 2) // bounded by predicate
                .forEach(System.out::println);
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
}
