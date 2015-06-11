package nl.rug.search.cpptool.api;

import nl.rug.search.cpptool.api.data.Location;
import nl.rug.search.cpptool.api.util.MissingDataException;

import javax.annotation.Nonnull;
import java.util.Optional;

public interface Declaration {

    @Nonnull
    DeclType type();

    @Nonnull
    <T> Optional<T> data(final @Nonnull Class<T> dataClass);

    @Nonnull
    default <T> T dataUnchecked(final @Nonnull Class<T> dataClass) throws MissingDataException {
        return data(dataClass).orElseThrow(MissingDataException.supplier(this, dataClass));
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
        return this.type().check(this);
    }
}
