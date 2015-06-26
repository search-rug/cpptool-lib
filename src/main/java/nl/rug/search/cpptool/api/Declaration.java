package nl.rug.search.cpptool.api;

import nl.rug.search.cpptool.api.data.ContextHolder;
import nl.rug.search.cpptool.api.data.Location;
import nl.rug.search.cpptool.api.data.Named;
import nl.rug.search.cpptool.api.data.ParentContext;
import nl.rug.search.cpptool.api.util.MissingDataException;

import javax.annotation.Nonnull;
import java.util.Optional;

/**
 * Declarations are the leaf nodes of the declaration tree.
 * Each declaration is of a certain type ({@link #declarationType()}) and consists
 * of a number of data components ({@link #data(Class)}).
 * <br />
 * See {@link DeclType} for the types of declarations emitted by this library.
 *
 * @author David van Leusen <J.D.van.leusen@student.rug.nl>
 * @since 2015-06-24
 */
public interface Declaration {

    /**
     * @return the type of this declaration
     */
    @Nonnull
    DeclType declarationType();

    /**
     * Attempts to retrieve a given data component from this declaration.
     *
     * @param dataClass class of the requested data component
     * @param <T>       Interface type of the requested data component
     * @return the requested data component
     */
    @Nonnull
    <T> Optional<T> data(final @Nonnull Class<T> dataClass);

    /**
     * Utility method to retrieve a data component through {@link #data(Class)} that has to be present.
     *
     * @param dataClass class of the requested data component
     * @param <T>       Interface type of the requested data component
     * @return the requested data component
     * @throws MissingDataException
     */
    @Nonnull
    default <T> T dataUnchecked(final @Nonnull Class<T> dataClass) throws MissingDataException {
        return this.data(dataClass).orElseThrow(MissingDataException.supplier(this, dataClass));
    }

    /**
     * Utility method to test for the presence of a certain data component.
     *
     * @param dataClass class of the data component to look for
     * @return whether the data component is present for this declaration
     */
    default boolean has(final @Nonnull Class<?> dataClass) {
        return this.data(dataClass).isPresent();
    }

    /**
     * @return the location where this declaration was defined, if known
     */
    @Nonnull
    default Optional<Location> location() {
        return this.data(Location.class);
    }

    /**
     * @return the external name of this declaration, not all declarations have a name
     */
    @Nonnull
    default Optional<String> name() {
        return this.data(Named.class).map(Named::name);
    }

    /**
     * @return the context in which this declaration was defined
     */
    @Nonnull
    default DeclContext parentContext() {
        return this.dataUnchecked(ParentContext.class).parent();
    }

    /**
     * @return the context created by this declaration, if the declaration creates such a context
     */
    @Nonnull
    default Optional<DeclContext> selfContext() {
        return this.data(ContextHolder.class).map(ContextHolder::context);
    }

    /**
     * Utility method to check if the declaration has all the data components that are expected for its declaration-type
     *
     * @return whether this declaration is valid
     * @see DeclType#check(Declaration)
     */
    default boolean validateState() {
        return this.declarationType().check(this);
    }
}
