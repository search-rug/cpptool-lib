package nl.rug.search.cpptool.api;

import javax.annotation.Nonnull;
import java.io.PrintStream;
import java.util.Optional;

/**
 *
 */
public interface DeclContext {
    /**
     * @return
     */
    @Nonnull
    Iterable<DeclContext> children();

    /**
     * @return
     */
    @Nonnull
    Iterable<Declaration> declarations();

    /**
     * @return
     */
    @Nonnull
    Optional<Declaration> definition();

    /**
     * @return
     * @throws java.util.NoSuchElementException
     */
    @Nonnull
    DeclContext parent();

    /**
     * @return
     */
    @Nonnull
    Optional<String> name();

    /**
     * @param out
     */
    void dump(final @Nonnull PrintStream out);
}
