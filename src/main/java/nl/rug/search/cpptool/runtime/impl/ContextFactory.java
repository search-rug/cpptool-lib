package nl.rug.search.cpptool.runtime.impl;

import com.google.common.collect.Maps;
import nl.rug.search.cpptool.api.DeclContainer;
import nl.rug.search.cpptool.api.DeclContext;
import nl.rug.search.cpptool.api.DeclType;
import nl.rug.search.cpptool.api.SourceFile;
import nl.rug.search.cpptool.api.data.ContextHolder;
import nl.rug.search.cpptool.api.data.Location;
import nl.rug.search.cpptool.api.data.Named;
import nl.rug.search.cpptool.api.data.ParentContext;
import nl.rug.search.cpptool.runtime.data.ContextHolderData;
import nl.rug.search.cpptool.runtime.data.NamedData;
import nl.rug.search.cpptool.runtime.data.ParentContextData;
import nl.rug.search.cpptool.runtime.mutable.MDeclContext;
import nl.rug.search.cpptool.runtime.mutable.MDeclaration;
import nl.rug.search.cpptool.runtime.mutable.MSourceFile;
import nl.rug.search.cpptool.runtime.mutable.MType;
import nl.rug.search.cpptool.runtime.util.ContextPath;
import nl.rug.search.proto.Base;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Stack;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

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
        final MDeclContext mirror = retrieveMirrorContext(context);

        //DeclContext = parent Context
        decl.insertData(ParentContext.class, ParentContextData.build(context));

        //If name, Named data
        name.map(NamedData::build).ifPresent((data) -> decl.insertData(Named.class, data));

        if (type.hasContext == DeclType.HasContext.TRUE) {
            //Create contexts in both contexts
            mirror.getOrCreateSubcontext(name, Optional.of(decl));
            decl.insertData(ContextHolder.class, ContextHolderData.build(
                    context.getOrCreateSubcontext(name, Optional.of(decl))
            ));
        }

        mirror.insertDeclaration(decl);
        context.insertDeclaration(decl);

        return decl;
    }

    @Nonnull
    public MDeclContext createTopContext() {
        return new InternalDeclContext(ContextPath.from(""), null, Optional.empty());
    }

    public void setContextMirror(@Nonnull MDeclContext context) {
        this.mirrorContext = context;
    }

    @Nonnull
    public MSourceFile createFile(String path) {
        return new InternalSourceFile(path);
    }

    @Nonnull
    public MType createType(Base.ScopedName name, Optional<Location> loc,
                            boolean isStronglyDefined, DynamicLookup<MDeclaration> decl) {
        return new InternalType(name, decl, loc, isStronglyDefined);
    }

    private MDeclContext retrieveMirrorContext(MDeclContext origContext) {
        checkState(mirrorContext != null, "mirror not initialized");

        //Rewind original context stack
        final Stack<InternalDeclContext> contextStack = new Stack<>();
        InternalDeclContext context = (InternalDeclContext) origContext;
        while (context != primaryContext) {
            contextStack.push(context);
            context = (InternalDeclContext) context.parent();
        }

        //Dive into mirror context stack
        InternalDeclContext mirrorContext = (InternalDeclContext) this.mirrorContext;
        while (!contextStack.isEmpty()) {
            mirrorContext = mirrorContext.getOrCreateSubContext(contextStack.pop());
        }

        return mirrorContext;
    }

    @Nonnull
    public MDeclContext getDeclContext(ContextPath path) {
        MDeclContext context = primaryContext;
        for (String subPath : path.segments()) {
            context = context.getOrCreateSubcontext(Optional.of(subPath), Optional.empty());
        }
        return context;
    }
}
