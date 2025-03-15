package Collections;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LearnMap {
    Map<String, Integer> map = new HashMap<>();

    {
        map.put("a", 3);
        map.put("b", 2);
        map.put("c", 1);
    }
    //streaming a map
    public static void streamMap()  {
        System.out.println("Streaming a map:");
        Stream<Map.Entry<String, Integer>> stream = new LearnMap().map.entrySet().stream();
        stream.forEach(x -> System.out.println(x.getKey() + " " + x.getValue()));

        Stream<String> stream2 = new LearnMap().map.keySet().stream();
        Stream<Integer> stream3 = new LearnMap().map.values().stream();
    }

    public static void filterMap()  {
        System.out.println("Filtering a map:");
        Stream<Map.Entry<String, Integer>> stream = new LearnMap().map.entrySet().stream();
        stream
                .filter(x -> x.getValue() > 1)
                .forEach(x -> System.out.println(x.getKey() + " " + x.getValue()));

        System.out.println("Filtering a map with a predicate:");
        Map<String, Integer> map = new LearnMap().map;
        map.entrySet().stream()
                .filter(x -> x.getValue() > 1)
                .forEach(x -> System.out.println(x.getKey() + " " + x.getValue()));
    }

    // Transform each element of the stream, to a new type of map or change value
    public static void mapping()    {
        System.out.println("Mapping a map:");
        Map<String, Integer> mapped = new LearnMap().map.entrySet().stream()
                .collect(HashMap::new, (m, v) -> m.put(v.getKey(), v.getValue() + 1), HashMap::putAll);
    }

    public static void iterateMap() {
        System.out.println("Iterating a map:");
        Map<String, Integer> map = new LearnMap().map;
        map.forEach((k, v) -> System.out.println(k + " " + v));
    }

    public static void sortMap()    {
        System.out.println("Sorting a map:");
        Map<String, Integer> map = new LearnMap().map;
        map.entrySet().stream()
                .sorted(Map.Entry.comparingByValue())
                .forEach(x -> System.out.println(x.getKey() + " " + x.getValue()));

        Map<String, Integer> map2 = new LearnMap().map;
        map2.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }

    class A {
        void a() {
            System.out.println("a");
        }
    }

    class B extends A {
        void b() {
            System.out.println("b");
        }
    }
        class C extends B {
            void c() {
                System.out.println("c");
            }
        }

        class D extends C {
            void d() {
                System.out.println("d");
            }
        }

    public static void main(String[] args)  {
        streamMap();
        filterMap();
        mapping();
        iterateMap();
        sortMap();

        NavigableMap<String, String> myMap = new TreeMap<>(Comparator.reverseOrder());
        myMap.put("a", "apple");
        myMap.put("b", "boy");
        myMap.put("c", "cat");
        myMap.put("aa", "apple1");
        myMap.put("bb", "boy1");
        myMap.put("cc", "cat1");

        myMap.pollLastEntry();
        myMap.pollFirstEntry();

        NavigableMap<String, String> tailMap = myMap.tailMap("bb", false);

        System.out.println(tailMap.pollFirstEntry());
        System.out.println(myMap.size());
        System.out.println(myMap.comparator());

        B b = new LearnMap().new C();
        A a = b;

        if (a instanceof B b1) {
            b1.b();
        }

        if (a instanceof C c1) {
            c1.c();
        }

        if (a instanceof D d1) {
            d1.d();
        }
    }
}
