package PatternMatching;

public class UnNamedPattern {

    void test(Employee employee) {
        switch (employee) {
            case Salaried s -> System.out.println("String with length 1: " + s);
            case Freelance _, Intern _ -> System.out.println("String: Other");
            default -> System.out.println("Unknown type: ");
        }
    }
}

sealed interface Employee permits Salaried, Freelance, Intern {
    double getSalary();
}

record Salaried(double salary) implements Employee {
    @Override
    public double getSalary() {
        return salary;
    }
}

record Freelance(double hourlyRate, int hoursWorked) implements Employee {
    @Override
    public double getSalary() {
        return hourlyRate * hoursWorked;
    }
}

record Intern() implements Employee {
    @Override
    public double getSalary() {
        return 0; // Interns typically have no salary
    }
}
