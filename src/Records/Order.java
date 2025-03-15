package Records;

import java.time.LocalDateTime;

public record Order<T> (T id, double amount, String... items) {
    public Order {
        if (amount < 0) {
            throw new IllegalArgumentException("Amount cannot be negative");
        }
        if (items.length == 0) {
            throw new IllegalArgumentException("At least one item is required");
        }
        for (String item : items) {
            if (item == null || item.isEmpty()) {
                throw new IllegalArgumentException("Item cannot be null or empty");
            }
        }
    }

    public Order(T id) {
        this(id, 10000.0);
       new OrderDetails(LocalDateTime.now());
    }

    public boolean isLargeOrder() {
        return amount > 1000;
    }

    // Nested Record class
    record OrderDetails(LocalDateTime deliveryDate){
        public OrderDetails {
            if (deliveryDate == null) {
                throw new IllegalArgumentException("Delivery date cannot be null");
            } else {
                System.out.println("Order Details: " + deliveryDate);
            }
        }
    }
}
