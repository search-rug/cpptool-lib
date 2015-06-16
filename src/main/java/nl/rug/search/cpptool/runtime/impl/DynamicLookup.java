package nl.rug.search.cpptool.runtime.impl;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.function.Supplier;

public interface DynamicLookup<T> extends Supplier<T>, com.google.common.base.Supplier<T> {
    @Nonnull
    T get();

    @Nonnull
    Optional<T> toOptional();
}
