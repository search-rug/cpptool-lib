package nl.rug.search.cpptool.api.data;

import nl.rug.search.cpptool.api.Attached;
import nl.rug.search.cpptool.api.DeclType;
import nl.rug.search.cpptool.api.Type;

import javax.annotation.Nonnull;

/**
 * Data component that is attached to all {@link DeclType#TYPEDEF} declarations.
 *
 * @author David van Leusen <J.D.van.leusen@student.rug.nl>
 * @see <a href="http://en.cppreference.com/w/c/language/typedef">typedef declaration</a>
 * @since 2015-06-24
 */
public interface Typedef extends Attached {
    @Nonnull
    Type getMappedType();
}
