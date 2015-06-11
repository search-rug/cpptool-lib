package nl.rug.search.cpptool.api.data;

import nl.rug.search.cpptool.api.DeclType;
import nl.rug.search.cpptool.api.Declaration;
import nl.rug.search.cpptool.api.Type;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

public interface Record extends Attached {

    /**
     * @param decl
     * @return
     * @throws IllegalArgumentException
     */
    @Nonnull
    static Record get(@Nonnull final Declaration decl) throws IllegalArgumentException {
        checkNotNull(decl, "decl == NULL");
        checkArgument(decl.type() == DeclType.RECORD, "Declaration is not a record");
        return decl.dataUnchecked(Record.class);
    }

    @Nonnull
    Type type();
}
