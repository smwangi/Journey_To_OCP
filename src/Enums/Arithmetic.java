package Enums;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum Arithmetic implements EnumInterface {
    PLUS {
        public int apply(int a, int b) {
            return a + b;
        }
    },
    MINUS {
        public int apply(int a, int b) {
            return a - b;
        }
    };

    public static void main(String[] arg) {
        int sum = Arithmetic.PLUS.apply(2, 5);
        int minus = Arithmetic.MINUS.apply(5, 4);
        System.out.println("Sum: " + sum + " AND Minus: " + minus);

        List<EnumInterface> ops = new ArrayList<>(Arrays.asList(Arithmetic.values()));
        ops.forEach(x -> System.out.println(x.apply(7, 4)));
    }
}