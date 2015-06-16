package nl.rug.search.cpptool.runtime.mutable;

import nl.rug.search.cpptool.api.DeclContext;
import nl.rug.search.cpptool.runtime.impl.DynamicLookup;

import java.util.Optional;

public interface MDeclContext extends DeclContext {
    DynamicLookup<MDeclaration> getDeclaration(String name);

    MDeclContext getOrCreateSubcontext(Optional<String> name, Optional<MDeclaration> decl);

    void insertDeclaration(MDeclaration decl);
}
