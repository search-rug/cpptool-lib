package nl.rug.search.cpptool.api.data;

import nl.rug.search.cpptool.api.DeclType;
import nl.rug.search.cpptool.api.Declaration;
import nl.rug.search.cpptool.api.Type;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public interface Variable extends Attached {
    /**
     * @param decl
     * @return
     * @throws IllegalArgumentException
     */
    @Nonnull
    static Variable get(final @Nonnull Declaration decl) throws IllegalArgumentException {
        checkNotNull(decl, "decl == NULL");
        checkArgument(decl.type() == DeclType.VARIABLE, "Declaration is not a variable");
        return decl.dataUnchecked(Variable.class);
    }

    @Nonnull
    Type type();

    @Nonnull
    String name(); //TODO: maybe Named attribute
}
