package nl.rug.search.cpptool.api.data;

import nl.rug.search.cpptool.api.Type;

import javax.annotation.Nonnull;

/**
 * Data component that is stored in the corresponding {@link Template} for each unique template parameter.
 *
 * @author David van Leusen <J.D.van.leusen@student.rug.nl>
 * @see <a href="http://en.cppreference.com/w/cpp/language/template_parameters">template parameters</a>
 * @since 2015-06-24
 */
public interface TemplateParameter {
    @Nonnull
    Type type();

    @Nonnull
    default String identifier() {
        return this.type().name();
    }
}
