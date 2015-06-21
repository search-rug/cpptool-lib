package nl.rug.search.cpptool.runtime.impl;

import com.google.common.collect.Lists;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

public class RelocatableProperty<T> implements DynamicLookup<T> {
    private final List<RelocatableProperty<T>> linkedReferences = Lists.newLinkedList();
    private Optional<T> val = Optional.empty();

    @Nonnull
    public static <T> RelocatableProperty<T> wrap(final @Nonnull T content) {
        final RelocatableProperty<T> relocatable = new RelocatableProperty<>();
        relocatable.set(checkNotNull(content, "content == NULL"));
        return relocatable;
    }

    @Nonnull
    public static <T> RelocatableProperty<T> empty() {
        return new RelocatableProperty<>();
    }

    public void set(T val) {
        this.val = Optional.of(val);
        linkedReferences.forEach((ref) -> ref.val = this.val);
    }

    public void link(DynamicLookup<T> otherRef) {
        if (otherRef instanceof RelocatableProperty && otherRef != this) {
            linkedReferences.add((RelocatableProperty<T>) otherRef);
            ((RelocatableProperty<T>) otherRef).val = this.val;
        }
    }

    @Nonnull
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
