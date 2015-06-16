package nl.rug.search.cpptool.api.data;

import nl.rug.search.cpptool.api.Type;

import javax.annotation.Nonnull;

public interface TemplateParameter {
    @Nonnull
    Type type();

    @Nonnull
    default String identifier() {
        return this.type().name();
    }
}
