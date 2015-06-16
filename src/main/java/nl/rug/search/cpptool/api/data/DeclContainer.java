package nl.rug.search.cpptool.api.data;

import nl.rug.search.cpptool.api.DeclContext;

import javax.annotation.Nonnull;

public interface DeclContainer {
    @Nonnull
    DeclContext context();
}
