package nl.rug.search.cpptool.api;

import nl.rug.search.cpptool.api.data.Location;

import javax.annotation.Nonnull;
import java.util.Optional;

/**
 * @author David van Leusen <J.D.van.leusen@student.rug.nl>
 * @since 2015-06-24
 */
public interface Type {

    /**
     * @return Fully qualified name of the type
     */
    @Nonnull
    String name();

    /**
     * @return The declaration that defines this type, if the type is not defined within the input data, then no
     * declaration will be attached.
     */
    @Nonnull
    Optional<Declaration> declaration();

    /**
     * @return The location where the type is defined, not defined for built-in types.
     */
    @Nonnull
    Optional<Location> location();

    /**
     * Modifiers are ordered depending on their syntactic ordered
     * int *& == [REFERENCE, POINTER] to int, aka reference to pointer of int
     *
     * @return The set of modifiers on this type
     */
    @Nonnull
    Iterable<Modifier> modifiers();

    /**
     * @return The type without any {@link #modifiers()}.
     */
    @Nonnull
    Type unwrappedType();

    /**
     * @return Whether the type is strongly defined in this use-case, uses of a forward-declaration result in
     * a weakly-defined type.
     */
    boolean isStronglyDefined();

    /**
     * @return Whether this is a compiler built-in type like primitives or intrinsics.
     */
    default boolean isBuiltin() {
        return !this.location().isPresent();
    }

    enum Modifier {
        POINTER,
        REFERENCE
    }
}
