package NestedClasses;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class Food {
    int price;
    public Food() {
    }

    public Food(int price) {
        this.price = price;
    }

    Popcorn p = new Popcorn() {
        @Override
        public void pop() {
            super.pop();
        }
        public void sizzle() {
            System.out.println("Sizzle");
        }
    };

    Cookable cookable = new Cookable() {
        @Override
        public void cook() {
            System.out.println("Cook me");
        }

        @Override
        public void boil() {
            System.out.println("Boil me");
        }
    };

    public static void main(String... args) {
        Food f = new Food();
        f.p.pop();
        f.cookable.cook();
        f.cookable.boil();
        MyWonderfulClass myWonderfulClass = new MyWonderfulClass();
        myWonderfulClass.go();

        List<Food> food = new ArrayList<>();
        food.add(new Food(2));
        food.add(new Food(20));
        food.add(new Food(30));

        f.printImportantData(food, foo -> foo.price > 10);
    }

    public void printImportantData(List<Food> foos, Predicate<Food> predicate) {
        for (Food foo : foos) {
            if (predicate.test(foo)) {
                System.out.println(foo.price);
            }
        }
    }
}

class Popcorn {
    public void pop() {
        System.out.println("popcorn");
    }
}

class MyWonderfulClass {
    void go() {
        Bar b = new Bar();
        b.doStuff(() -> System.out.println("foofy"));
    }
}

interface Foo {
    void foof();
}

class Bar {
    void doStuff(Foo f) {
        f.foof();
    }
}

interface Cookable {
    void cook();
    void boil();
}