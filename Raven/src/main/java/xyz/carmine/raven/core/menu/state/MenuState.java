package xyz.carmine.raven.core.menu.state;

import java.util.function.UnaryOperator;

public class MenuState<T> {
    private T value;

    public MenuState(T defaultValue) {
        this.value = defaultValue;
    }

    public T get() {
        return value;
    }

    public void set(T value) {
        this.value = value;
    }

    public void update(UnaryOperator<T> updater) {
        this.value = updater.apply(this.value);
    }
}