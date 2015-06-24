package nl.rug.search.cpptool.api.data;

import nl.rug.search.cpptool.api.Attached;
import nl.rug.search.cpptool.api.DeclType;
import nl.rug.search.cpptool.api.Declaration;
import nl.rug.search.cpptool.api.Type;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Data component that is attached to all {@link DeclType#RECORD} declarations.
 *
 * @author David van Leusen <J.D.van.leusen@student.rug.nl>
 * @see <a href="http://en.cppreference.com/w/c/language/struct">struct declaration</a>
 * @see <a href="http://en.cppreference.com/w/c/language/union">union declaration</a>
 * @since 2015-06-24
 */
public interface Record extends Attached {

    /**
     * Utility function to access {@link Record} data in bulk.
     * <br />
     * Get all C-style record data in the DeclContainer:
     * {@code IterTools.stream(DeclContainer.context()).filter(T::isCRecord).map(Record::get)}
     *
     * @param decl record declaration
     * @return record data belonging to the given declaration
     * @throws IllegalArgumentException if the given declaration is not a record
     */
    @Nonnull
    static Record get(@Nonnull final Declaration decl) throws IllegalArgumentException {
        checkNotNull(decl, "decl == NULL");
        checkArgument(decl.declarationType() == DeclType.RECORD, "Declaration is not a record");
        return decl.dataUnchecked(Record.class);
    }

    @Nonnull
    Type type();

    @Nonnull
    Variant variant();

    enum Variant {
        CLASS,
        STRUCT,
        UNION
    }
}
