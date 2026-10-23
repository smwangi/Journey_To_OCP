package sealedclasses;

public class SealedClassMain {
    public static void main(String[] args) {
        Result<String> successResult = new Success<>("123");
        Result<String> failureResult = new Failure<>("Operation failed due to an error");

        Result<Integer> mappedSuccess = successResult
            .map(Integer::parseInt)
            .flatMap(value -> value > 100 
                ? new Success<>(value * 2) 
                : new Failure<>("Value is not greater than 100"));

        mappedSuccess.map(n -> n * 2)
            .map(n -> "Double of the value is: " + n);
            
    }
}
