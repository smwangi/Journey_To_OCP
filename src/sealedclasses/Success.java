package sealedclasses;

public final  class Success<T> implements Result<T> {
    private final T value;

    public Success(T value) {
        if (value == null) {
            throw new IllegalArgumentException("Value cannot be null");
        }
        this.value = value;
    }

    @Override
    public <U> Result<U> map(java.util.function.Function<? super T, ? extends U> mapper) {
        return new Success<>(mapper.apply(value));
    }

    @Override
    public <U> Result<U> flatMap(java.util.function.Function<? super T, ? extends Result<U>> mapper) {
        return mapper.apply(value);
    }

    @Override
    public T getValue() {
        return value;
    }

    @Override
    public boolean isSuccess() {
        return true;
    }

    @Override
    public boolean isFailure() {
        return false;
    }

    @Override
    public void ifpresentorElse(Object object, Object object2) {
        if (object != null) {
            System.out.println("Value: " + value);
        } else {
            System.out.println("No value present");
        }
    }

}
