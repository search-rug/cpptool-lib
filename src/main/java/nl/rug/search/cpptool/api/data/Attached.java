package nl.rug.search.cpptool.api.data;

import nl.rug.search.cpptool.api.Declaration;

import javax.annotation.Nonnull;

public interface Attached {
    @Nonnull
    Declaration decl();
}
