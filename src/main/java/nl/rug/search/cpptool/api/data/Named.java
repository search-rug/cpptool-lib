package nl.rug.search.cpptool.api.data;

import nl.rug.search.cpptool.api.DeclType;

import javax.annotation.Nonnull;

/**
 * Data component that is attached to all declarations that have names by which they can be addressed.
 * All declarations besides {@link DeclType#LAMBDA_FUNCTION} have this data.
 *
 * @author David van Leusen <J.D.van.leusen@student.rug.nl>
 * @since 2015-06-24
 */
public interface Named {
    @Nonnull
    String name();
}
