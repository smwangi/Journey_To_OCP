package EnumTutorial;

public class EnumMain {
    public static void main(String[] args) {
        // Example usage of the Arithmetic enum
        int sum = Arithmetic.PLUS.apply(2, 5);
        int minus = Arithmetic.MINUS.apply(5, 4);
        System.out.println("Sum: " + sum + " AND Minus: " + minus);

        // Using the EnumInterface with a list of operations
        for (Arithmetic op : Arithmetic.values()) {
            System.out.println(op.apply(7, 4));
        }

        // Example usage of the Operation enum
        int opSum = Operation.PLUS.apply(3, 4);
        int opMinus = Operation.MINUS.apply(10, 5);
        System.out.println("Operation Sum: " + opSum + " AND Operation Minus: " + opMinus);
    }
}
