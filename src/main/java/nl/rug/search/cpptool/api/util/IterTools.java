package nl.rug.search.cpptool.api.util;

import com.google.common.collect.FluentIterable;
import nl.rug.search.cpptool.api.DeclContainer;
import nl.rug.search.cpptool.api.Declaration;

import javax.annotation.Nonnull;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static com.google.common.base.Preconditions.checkNotNull;

public class IterTools {
    private IterTools() {
        //This class shouldn't be instantiated.
        throw new ExceptionInInitializerError(IterTools.class.getCanonicalName() + " cannot be instantiated.");
    }

    @Nonnull
    public static <T> Stream<T> stream(final @Nonnull Iterable<T> it) {
        return StreamSupport.stream(checkNotNull(it, "it == NULL").spliterator(), false);
    }

    @Nonnull
    public static Stream<Declaration> stream(final @Nonnull DeclContainer container) {
        return stream(ContextTools.traverseDecls(checkNotNull(container, "container == NULL").context()));
    }

    @Nonnull
    public static <T> FluentIterable<T> fluent(final @Nonnull Iterable<T> it) {
        return FluentIterable.from(checkNotNull(it, "it == NULL"));
    }

    @Nonnull
    public static FluentIterable<Declaration> fluent(final @Nonnull DeclContainer container) {
        return fluent(ContextTools.traverseDecls(checkNotNull(container, "container == NULL").context()));
    }
}
