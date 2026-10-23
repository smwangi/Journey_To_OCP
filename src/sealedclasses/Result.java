package sealedclasses;


public sealed interface Result<T> permits Success, Failure {

    <U> Result<U> map(java.util.function.Function<? super T, ? extends U> mapper);
    <U> Result<U> flatMap(java.util.function.Function<? super T, ? extends Result<U>> mapper);

    // Method to get the value of the result
    T getValue();

    // Method to check if the result is successful
    boolean isSuccess();

    // Method to check if the result is a failure
    boolean isFailure();
    void ifpresentorElse(Object object, Object object2);
    
}
