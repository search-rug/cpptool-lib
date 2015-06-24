package nl.rug.search.cpptool.api.data;

import nl.rug.search.cpptool.api.DeclContext;

import javax.annotation.Nonnull;

/**
 * Data component that represents a one to one relationship between a declaration and a declcontext.
 * In practise this means the declaration can contain other declarations, such as records and functions.
 *
 * @author David van Leusen <J.D.van.leusen@student.rug.nl>
 * @since 2015-06-24
 */
public interface ContextHolder {
    @Nonnull
    DeclContext context();
}
