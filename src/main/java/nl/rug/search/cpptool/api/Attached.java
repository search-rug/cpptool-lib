package nl.rug.search.cpptool.api;

import javax.annotation.Nonnull;

/**
 * This interface marks any data component that forms a one-to-one relationship to a declaration type.
 *
 * @author David van Leusen <J.D.van.leusen@student.rug.nl>
 * @since 2015-06-24
 */
public interface Attached {
    @Nonnull
    Declaration decl();
}
