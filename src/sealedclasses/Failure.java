package sealedclasses;

public final  class Failure<T> implements Result<T> {
    private final String errorMessage;

    public Failure(String errorMessage) {
        if (errorMessage == null || errorMessage.isEmpty()) {
            throw new IllegalArgumentException("Error message cannot be null or empty");
        }
        this.errorMessage = errorMessage;
    }

    @Override
    public <U> Result<U> map(java.util.function.Function<? super T, ? extends U> mapper) {
        return new Failure<>(errorMessage);
    }

    @Override
    public <U> Result<U> flatMap(java.util.function.Function<? super T, ? extends Result<U>> mapper) {
        return new Failure<>(errorMessage);
    }

    @Override
    public T getValue() {
        throw new IllegalStateException("Cannot get value from a failure result");
    }

    @Override
    public boolean isSuccess() {
        return false;
    }

    @Override
    public boolean isFailure() {
        return true;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    @Override
    public void ifpresentorElse(Object object, Object object2) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'ifpresentorElse'");
    }

}
