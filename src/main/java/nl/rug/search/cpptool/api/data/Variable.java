package nl.rug.search.cpptool.api.data;

import nl.rug.search.cpptool.api.Attached;
import nl.rug.search.cpptool.api.DeclType;
import nl.rug.search.cpptool.api.Declaration;
import nl.rug.search.cpptool.api.Type;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Data component that is attached to all {@link DeclType#VARIABLE} declarations.
 *
 * @author David van Leusen <J.D.van.leusen@student.rug.nl>
 * @since 2015-06-24
 */
public interface Variable extends Attached {
    /**
     * Utility function to access {@link Variable} data in bulk.
     * <br />
     * Get all variable data in the DeclContainer:
     * {@code IterTools.stream(DeclContainer.context()).filter(T::isVariable).map(Variable::get)}
     *
     * @param decl variable declaration
     * @return variable data belonging to the given declaration
     * @throws IllegalArgumentException if the given declaration is not a variable
     */
    @Nonnull
    static Variable get(final @Nonnull Declaration decl) throws IllegalArgumentException {
        checkNotNull(decl, "decl == NULL");
        checkArgument(decl.declarationType() == DeclType.VARIABLE, "Declaration is not a variable");
        return decl.dataUnchecked(Variable.class);
    }

    @Nonnull
    Type type();

    @Nonnull
    VariableKind kind();

    enum VariableKind {
        CLASS_MEMBER,
        LOCAL,
        GLOBAL,
        PARAMETER
    }
}
