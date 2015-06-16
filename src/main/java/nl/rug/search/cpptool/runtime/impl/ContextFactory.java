package nl.rug.search.cpptool.runtime.impl;

import nl.rug.search.cpptool.api.DeclType;
import nl.rug.search.cpptool.runtime.mutable.MDeclContext;
import nl.rug.search.cpptool.runtime.mutable.MDeclaration;
import nl.rug.search.cpptool.runtime.mutable.MSourceFile;

import javax.annotation.Nonnull;
import java.util.Optional;

import static nl.rug.search.cpptool.runtime.util.Debug.NYI;

public class ContextFactory {
    private final MDeclContext primaryContext = createTopContext();
    private MDeclContext mirrorContext = null;

    @Nonnull
    public MDeclaration createDeclaration(@Nonnull MDeclContext context, @Nonnull DeclType type) {
        return createDeclaration(context, type, Optional.empty());
    }

    @Nonnull
    public MDeclaration createDeclaration(@Nonnull MDeclContext context,
                                          @Nonnull DeclType type, @Nonnull Optional<String> name) {
        throw NYI();
    }

    @Nonnull
    public MDeclContext createTopContext() {
        return new InternalDeclContext(null);
    }

    public void setContextMirror(@Nonnull MDeclContext context) {
        this.mirrorContext = context;
    }

    @Nonnull
    public MSourceFile newFile(String path) {
        return new InternalSourceFile(path);
    }
}
