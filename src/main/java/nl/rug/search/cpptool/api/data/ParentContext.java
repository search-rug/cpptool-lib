package nl.rug.search.cpptool.api.data;

import nl.rug.search.cpptool.api.DeclContext;

import javax.annotation.Nonnull;

/**
 * Data component that specifies the parent context of the declaration.
 * This is simply a container and each declaration must have this component.
 *
 * @author David van Leusen <J.D.van.leusen@student.rug.nl>
 * @since 2015-06-24
 */
public interface ParentContext {
    @Nonnull
    DeclContext parent();
}
