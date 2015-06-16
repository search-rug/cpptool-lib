package nl.rug.search.cpptool.runtime.impl;

import nl.rug.search.cpptool.api.DeclContainer;
import nl.rug.search.cpptool.api.DeclContext;
import nl.rug.search.cpptool.api.DeclType;
import nl.rug.search.cpptool.api.SourceFile;
import nl.rug.search.cpptool.api.data.Location;
import nl.rug.search.cpptool.runtime.mutable.MDeclContext;
import nl.rug.search.cpptool.runtime.mutable.MDeclaration;
import nl.rug.search.cpptool.runtime.mutable.MSourceFile;
import nl.rug.search.cpptool.runtime.mutable.MType;
import nl.rug.search.proto.Base;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;

import static nl.rug.search.cpptool.runtime.util.Debug.NYI;

public class ContextFactory {
    private final MDeclContext primaryContext = createTopContext();
    private MDeclContext mirrorContext = null;

    @Nonnull
    public DeclContainer build(DeclContext topContext, List<SourceFile> files) {
        return new InternalDeclContainer(topContext, files);
    }

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
    public MSourceFile createFile(String path) {
        return new InternalSourceFile(path);
    }

    @Nonnull
    public MType createType(Base.ScopedName name, Optional<Location> loc, boolean isStronglyDefined) {
        //TODO: fill relocatableproperty if it exists at this point (do lookup in primaryContext)
        return new InternalType(name, new RelocatableProperty<>(), loc, isStronglyDefined);
    }

    @Nonnull
    public MDeclContext getDeclContext(List<String> pathSplitter) {
        return null;
    }
}
