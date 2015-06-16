package nl.rug.search.cpptool.runtime.mutable;

import nl.rug.search.cpptool.api.Type;
import nl.rug.search.cpptool.runtime.impl.DynamicLookup;

import javax.annotation.Nonnull;

public interface MType extends Type {
    void updateDeclaration(final @Nonnull MDeclaration decl);

    DynamicLookup<MDeclaration> getDeclaration();
}
