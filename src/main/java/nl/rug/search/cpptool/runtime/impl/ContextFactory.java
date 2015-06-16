package nl.rug.search.cpptool.runtime.impl;

import nl.rug.search.cpptool.api.DeclContainer;
import nl.rug.search.cpptool.api.DeclContext;
import nl.rug.search.cpptool.api.DeclType;
import nl.rug.search.cpptool.api.SourceFile;
import nl.rug.search.cpptool.api.data.ContextHolder;
import nl.rug.search.cpptool.api.data.Location;
import nl.rug.search.cpptool.api.data.Named;
import nl.rug.search.cpptool.runtime.data.ContextHolderData;
import nl.rug.search.cpptool.runtime.data.NamedData;
import nl.rug.search.cpptool.runtime.mutable.MDeclContext;
import nl.rug.search.cpptool.runtime.mutable.MDeclaration;
import nl.rug.search.cpptool.runtime.mutable.MSourceFile;
import nl.rug.search.cpptool.runtime.mutable.MType;
import nl.rug.search.proto.Base;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;

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
        final MDeclaration decl = new InternalDeclaration(type);
        final MDeclContext mirror = null;
        //TODO: resolve path in mirrorcontext
        //TODO: insert decl into both contexts

        //DeclContext = parent Context
        decl.insertData(DeclContext.class, context);

        //If name, Named data
        name.map(NamedData::build).ifPresent((data) -> decl.insertData(Named.class, data));

        if (type.hasContext == DeclType.HasContext.TRUE) {
            //Create contexts in both contexts
            //TODO: mirror.getOrCreateSubcontext(name, decl);
            decl.insertData(ContextHolder.class, ContextHolderData.build(
                    context.getOrCreateSubcontext(name, Optional.of(decl))
            ));
        }

        return decl;
    }

    @Nonnull
    public MDeclContext createTopContext() {
        return new InternalDeclContext(null, Optional.empty());
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
        //TODO: fill relocatableproperty if it exists at this point
        //TODO: do lookup in loc.get().file() for the context
        return new InternalType(name, new RelocatableProperty<>(), loc, isStronglyDefined);
    }

    @Nonnull
    public MDeclContext getDeclContext(List<String> pathSplitter) {
        MDeclContext context = primaryContext;
        for (String path : pathSplitter) {
            context = context.getOrCreateSubcontext(Optional.of(path), Optional.empty());
        }
        return context;
    }
}
