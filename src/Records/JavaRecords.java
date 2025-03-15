package Records;

import java.time.LocalDateTime;
import java.util.Arrays;

public class JavaRecords {
    public static void main(String[] args) {
        // Local Record class example, not recommended though
        record Person(String name, int age) {
        }

        //Creating an object of the record class
        Person person = new Person("sam", 25);
        System.out.println(person.name());

        PersonInterface personInterface = new PersonInterface() {
            @Override
            public PersonInt getPerson() {
                return new PersonInt("sam", 25);
            }
        };

        PersonInterface.PersonInt personInt = personInterface.getPerson();
        System.out.println(personInt.name()+" "+personInt.age());

        //Or
        PersonInterface.PersonInt personInt1 = new PersonInterface.PersonInt("sam", 25);
        System.out.println(personInt1.name());

        // Implementing the interface and providing the record object
        PersonInterface pp = () -> personInt1;
        System.out.println(pp.getPerson());

        // Using the record class
        Order<String> order = new Order<>("123", 40, "abc", "xyz");
        System.out.println(order.isLargeOrder());
        System.out.println(Arrays.toString(order.items()));
        Order.OrderDetails order1 = new Order.OrderDetails(LocalDateTime.now()); //<>(1, 1000.0, "abc", "xyz").;
        System.out.println(order1.deliveryDate());
    }

    // Declaring a record in interface
    // This feature allows you to encapsulate the data structures directly within interface
    interface PersonInterface {
        // Nested record class in interface
        record PersonInt(String name, int age) {
        }
        // Abstract method
        PersonInt getPerson();
    }
}
