package nl.rug.search.cpptool.api.data;

import javax.annotation.Nonnull;

/**
 * Data component representing a set of parameters attached to a declaration.
 * Note: besides functions lambdas can also have parameters, so the declaration might not be a function.
 *
 * @author David van Leusen <J.D.van.leusen@student.rug.nl>
 * @since 2015-06-24
 */
public interface ParamSet {

    @Nonnull
    Iterable<Variable> params();
}
