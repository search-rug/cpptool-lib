package nl.rug.search.cpptool.api;

import nl.rug.search.cpptool.api.data.Location;
import nl.rug.search.cpptool.api.util.MissingDataException;

import javax.annotation.Nonnull;
import java.util.Optional;

public interface Declaration {

    @Nonnull
    DeclType declarationType();

    @Nonnull
    <T> Optional<T> data(final @Nonnull Class<T> dataClass);

    @Nonnull
    default <T> T dataUnchecked(final @Nonnull Class<T> dataClass) throws MissingDataException {
        return this.data(dataClass).orElseThrow(MissingDataException.supplier(this, dataClass));
    }

    default boolean has(final @Nonnull Class<?> dataClass) {
        return this.data(dataClass).isPresent();
    }

    @Nonnull
    default Optional<Location> location() {
        return this.data(Location.class);
    }

    @Nonnull
    default DeclContext context() {
        return this.dataUnchecked(DeclContext.class);
    }

    default boolean validateState() {
        return this.declarationType().check(this);
    }
}
