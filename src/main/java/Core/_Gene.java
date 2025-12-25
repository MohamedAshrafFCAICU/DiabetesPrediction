package Core;

public interface _Gene<T> {
    T getValue();
    void setValue(T value);
    _Gene<T> copy();
}