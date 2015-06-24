package nl.rug.search.cpptool.api.data;

import nl.rug.search.cpptool.api.DeclType;
import nl.rug.search.cpptool.api.Type;

import javax.annotation.Nonnull;

/**
 * Data component that specifies the attached declaration as a template.
 * It is possible for this component to be attached to {@link DeclType#VARIABLE}, {@link DeclType#FUNCTION},
 * {@link DeclType#TYPEDEF} and {@link DeclType#RECORD}.
 *
 * @author David van Leusen <J.D.van.leusen@student.rug.nl>
 * @see <a href="http://en.cppreference.com/w/cpp/language/templates">templates</a>
 * @since 2015-06-24
 */
public interface Template {
    @Nonnull
    Iterable<Type> specializations();

    @Nonnull
    Iterable<TemplateParameter> params();
}
