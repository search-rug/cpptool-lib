package nl.rug.search.cpptool.runtime.util;

import nl.rug.search.cpptool.runtime.impl.DynamicLookup;

import java.util.Optional;

public interface Coerce {
    static <T> Iterable<T> coerce(Iterable<? extends T> it) {
        return (Iterable<T>) it;
    }

    static <T> Optional<T> coerce(Optional<? extends T> optional) {
        return (Optional<T>) optional;
    }

    static <T> DynamicLookup<T> coerce(DynamicLookup<? extends T> dyn) {
        return (DynamicLookup<T>) dyn;
    }
}
