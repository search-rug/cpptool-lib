package nl.rug.search.cpptool.api.data;

import nl.rug.search.cpptool.api.DeclType;
import nl.rug.search.cpptool.api.Declaration;
import nl.rug.search.cpptool.api.Type;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public interface Function extends Attached {

    /**
     * @param decl
     * @return
     * @throws IllegalArgumentException
     */
    @Nonnull
    static Function get(@Nonnull final Declaration decl) throws IllegalArgumentException {
        checkNotNull(decl, "decl == NULL");
        checkArgument(decl.type() == DeclType.FUNCTION, "Declaration is not a function");
        return decl.dataUnchecked(Function.class);
    }

    @Nonnull
    default ParamSet params() {
        return this.decl().dataUnchecked(ParamSet.class);
    }

    @Nonnull
    Type returnType();
}
