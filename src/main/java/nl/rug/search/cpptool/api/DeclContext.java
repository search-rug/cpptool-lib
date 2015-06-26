package nl.rug.search.cpptool.api;

import javax.annotation.Nonnull;
import java.io.PrintStream;
import java.util.Optional;

/**
 * A node in the declaration tree, each node can contain several sub-trees that can be accessed
 * through {@link #children()}, as well as a set of declarations that can be accessed through {@link #declarations()}.
 *
 * @author David van Leusen <J.D.van.leusen@student.rug.nl>
 * @since 2015-06-24
 */
public interface DeclContext {
    /**
     * @return All declaration contexts that are considered direct children of this node.
     */
    @Nonnull
    Iterable<DeclContext> children();

    /**
     * @return All declarations contained in this declaration tree node.
     */
    @Nonnull
    Iterable<Declaration> declarations();

    /**
     * @return The declaration that created this declaration context, if it is created through that manner.
     */
    @Nonnull
    Optional<Declaration> definition();

    /**
     * @return The declaration tree node that this node is a child of.
     * @throws java.util.NoSuchElementException if called on the global context
     */
    @Nonnull
    DeclContext parent();

    /**
     * @return The name by which this context can be identified within its parent node, if such a thing exists.
     */
    @Nonnull
    Optional<String> name();

    /**
     * Utility function to print the declaration tree to the given output stream.
     *
     * @param out stream to print the representative tree to.
     */
    void dump(final @Nonnull PrintStream out);

    /**
     * @return whether this declaration context is a lambda function
     * @see <a href="http://en.cppreference.com/w/cpp/language/lambda">lambda functions</a>
     */
    default boolean isLambda() {
        // Lambdas have no name but do have a definition
        // Global context has no name and no definition
        return !name().isPresent() && definition().isPresent();
    }
}
