package nl.rug.search.cpptool.runtime.impl;

import javax.annotation.Nonnull;
import java.util.Optional;

public class RelocatableProperty<T> implements DynamicLookup<T> {
    private Optional<T> val = Optional.empty();

    public void set(T val) {
        this.val = Optional.of(val);
    }

    @Override
    public T get() {
        return this.val.get();
    }

    @Nonnull
    @Override
    public Optional<T> toOptional() {
        return val;
    }

    @Override
    public String toString() {
        return val.toString();
    }
}
