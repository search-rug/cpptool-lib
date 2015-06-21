package nl.rug.search.cpptool.runtime.mutable;

import nl.rug.search.cpptool.runtime.impl.DynamicLookup;

public interface Redirectable<T> {
    DynamicLookup<T> ref();

    void setRedirect(T redirect);

    /**
     * Links the state of other to this, meaning that if this redirectable changes,
     * other changes as well.
     *
     * @param other
     */
    default void link(DynamicLookup<T> other) {
        throw new UnsupportedOperationException("Linking is not appropriate for this kind of redirectable");
    }
}
