package nl.rug.search.cpptool.runtime.mutable;

import nl.rug.search.cpptool.api.Declaration;

import javax.annotation.Nonnull;

public interface MDeclaration extends Declaration, Redirectable<MDeclaration> {

    <T> void insertData(final @Nonnull Class<T> dataClass, final @Nonnull T data);

    @Nonnull
    String signature();
}
