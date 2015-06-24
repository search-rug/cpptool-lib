package nl.rug.search.cpptool.api.data;

import nl.rug.search.cpptool.api.Attached;
import nl.rug.search.cpptool.api.DeclType;
import nl.rug.search.cpptool.api.Declaration;
import nl.rug.search.cpptool.api.Type;

import javax.annotation.Nonnull;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Data component that is attached to all {@link DeclType#FUNCTION} declarations.
 *
 * @author David van Leusen <J.D.van.leusen@student.rug.nl>
 * @see <a href="http://en.cppreference.com/w/c/language/function_definition">function definitions</a>
 * @since 2015-06-24
 */
public interface Function extends Attached {

    /**
     * Utility function to access {@link Function} data in bulk.
     * <br />
     * Get all Function data in the DeclContainer
     * {@code IterTools.stream(DeclContainer.context()).filter(T::isFunction).map(Function::get)}
     *
     * @param decl function declaration
     * @return function data belonging to the given declaration
     * @throws IllegalArgumentException if the given declaration is not a function
     */
    @Nonnull
    static Function get(@Nonnull final Declaration decl) throws IllegalArgumentException {
        checkNotNull(decl, "decl == NULL");
        checkArgument(decl.declarationType() == DeclType.FUNCTION, "Declaration is not a function");
        return decl.dataUnchecked(Function.class);
    }

    @Nonnull
    default ParamSet params() {
        return this.decl().dataUnchecked(ParamSet.class);
    }

    @Nonnull
    Type returnType();

    @Nonnull
    Optional<Location> body();
}
