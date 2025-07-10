package PatternMatching;

public interface Shape {

    public static double getPerimeter(Shape shape) throws IllegalArgumentException {
        return switch (shape) {
            case Circle c -> 2 * Math.PI * c.getRadius();
            case Rectangle r -> 2 * (r.getLength() + r.getWidth());
            case Triangle t -> t.getSideA() + t.getSideB() + t.getSideC();
            default -> throw new IllegalArgumentException("Unknown shape: " + shape);
        };
    }

    public static boolean bigEnoughRect(Shape shape) {
        if (!(shape instanceof Rectangle rectangle)) {
            throw new IllegalArgumentException("Shape must be a Rectangle");
        }
        return rectangle.getLength() > 10 && rectangle.getWidth() > 10;
    }

    public static void test(Object obj) {
        switch (obj) {
            case String s when s.length() == 1 -> System.out.println("Shape: " + s.getClass().getName() + " with length 1");
            case String s -> System.out.println("String: " + s);
            case Integer i -> System.out.println("Integer: " + i);
            case Double d -> System.out.println("Double: " + d);
            case null -> System.out.println("Null object");
            default -> System.out.println("Unknown type: " + obj.getClass().getName());
        }
    }
}

class Circle implements Shape {
    private final double radius;

    public Circle(double radius) {
        if (radius <= 0) {
            throw new IllegalArgumentException("Radius must be positive");
        }
        this.radius = radius;
    }

    public double getRadius() {
        return radius;
    }
}

class Rectangle implements Shape {
    private final double length;
    private final double width;

    public Rectangle(double length, double width) {
        if (length <= 0 || width <= 0) {
            throw new IllegalArgumentException("Length and width must be positive");
        }
        this.length = length;
        this.width = width;
    }

    public double getLength() {
        return length;
    }

    public double getWidth() {
        return width;
    }
}

class Triangle implements Shape {
    private final double sideA;
    private final double sideB;
    private final double sideC;

    public Triangle(double sideA, double sideB, double sideC) {
        if (sideA <= 0 || sideB <= 0 || sideC <= 0) {
            throw new IllegalArgumentException("Sides must be positive");
        }
        if (sideA + sideB <= sideC || sideA + sideC <= sideB || sideB + sideC <= sideA) {
            throw new IllegalArgumentException("Invalid triangle sides");
        }
        this.sideA = sideA;
        this.sideB = sideB;
        this.sideC = sideC;
    }

    public double getSideA() {
        return sideA;
    }

    public double getSideB() {
        return sideB;
    }

    public double getSideC() {
        return sideC;
    }
}

class Main implements Shape {
    public static void main(String[] args) {
        Shape circle = new Circle(5);
        Shape rectangle = new Rectangle(4, 6);
        Shape triangle = new Triangle(3, 4, 5);

        getPerimeter(circle);
        getPerimeter(rectangle);
        getPerimeter(triangle);

        System.out.println("Circle perimeter: " + Shape.getPerimeter(circle));
        System.out.println("Rectangle perimeter: " + Shape.getPerimeter(rectangle));
        System.out.println("Triangle perimeter: " + Shape.getPerimeter(triangle));
    }

    public static double getPerimeter(Shape shape) {
        switch (shape) {
            case Circle c -> {
                return 2 * Math.PI * c.getRadius();
            }
            case Rectangle r -> {
                return 2 * (r.getLength() + r.getWidth());
            }
            case Triangle t -> {
                return t.getSideA() + t.getSideB() + t.getSideC();
            }
            default -> throw new IllegalArgumentException("Unknown shape: " + shape);
        }
    }
}
