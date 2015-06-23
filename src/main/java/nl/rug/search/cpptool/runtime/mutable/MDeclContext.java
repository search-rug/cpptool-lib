package nl.rug.search.cpptool.runtime.mutable;

import nl.rug.search.cpptool.api.DeclContext;
import nl.rug.search.cpptool.runtime.util.ContextPath;

import java.util.Optional;

public interface MDeclContext extends DeclContext, Redirectable<MDeclContext> {
    Optional<MDeclaration> getDeclaration(String name);

    ContextPath getPath();

    MDeclContext getOrCreateSubcontext(Optional<String> name, Optional<MDeclaration> decl);

    void setDeclaration(MDeclaration decl);

    void insertDeclaration(MDeclaration decl);
}
