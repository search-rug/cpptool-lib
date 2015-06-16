package nl.rug.search.cpptool.api.data;

import nl.rug.search.cpptool.api.Declaration;

import javax.annotation.Nonnull;

public interface Friends {
    /**
     * All declarations that are considered friends of this declaration.
     * <p>
     * Any declaration in this set is able to access the private members of this declaration.
     *
     * @return
     */
    @Nonnull
    Iterable<Declaration> friends();

    /**
     * All declarations that this declaration is a friend of.
     * <p>
     * This declaration is able to access private members of the Declarations in this set.
     *
     * @return
     */
    @Nonnull
    Iterable<Declaration> friendOf();
}
