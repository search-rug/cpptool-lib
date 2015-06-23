package nl.rug.search.cpptool.api;

import nl.rug.search.cpptool.api.data.Location;

import javax.annotation.Nonnull;
import java.util.Optional;

public interface Type {

    @Nonnull
    String name();

    @Nonnull
    Optional<Declaration> declaration();

    @Nonnull
    Optional<Location> location();

    @Nonnull
    Iterable<Modifier> modifiers();

    boolean isStronglyDefined();

    default boolean isBuiltin() {
        return !this.location().isPresent();
    }

    enum Modifier {
        POINTER,
        REFERENCE
    }
}
