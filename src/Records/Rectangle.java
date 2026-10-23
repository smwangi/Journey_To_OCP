package Records;

public record Rectangle(double length, double width) {
    // Compact constructor for Rectangle record
    // This constructor validates the input parameters and initializes the record fields.
    // It ensures that both length and width are positive values, throwing an exception if they are not.
    public Rectangle {
        if (length <= 0 || width <= 0) { // Canonical constructor validation
            throw new IllegalArgumentException("Length and width must be positive");
        }
    }

    public double area() {
        return length * width;
    }

    public double perimeter() {
        return 2 * (length + width);
    }

    // Nested record class example
    record Dimensions(double length, double width) {
        // Compact constructor for Dimensions record
        public Dimensions {
            if (length <= 0 || width <= 0) {
                throw new IllegalArgumentException("Length and width must be positive");
            }
        }
    }

    public Rectangle.Dimensions dimensions() {
        return new Dimensions(length, width);
    }

    public Rectangle Dimensions(double length, double width) {
        Dimensions dimensions = new Dimensions(length, width);
        double len = dimensions.length();
        double wid = dimensions.width();
        return new Rectangle(len, wid);
    }

    @Override
    public String toString() {
        return "Rectangle[length=" + length + ", width=" + width + "]";
    }
}
