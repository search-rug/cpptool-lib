package nl.rug.search.cpptool.api.data;

import nl.rug.search.cpptool.api.Type;

import javax.annotation.Nonnull;

public interface Template {
    @Nonnull
    Iterable<Type> specializations();

    @Nonnull
    Iterable<TemplateParameter> params();
}
