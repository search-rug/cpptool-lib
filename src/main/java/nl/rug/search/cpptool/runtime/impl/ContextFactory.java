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
import nl.rug.search.cpptool.proto.Base;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

public class ContextFactory {
    private final Map<MDeclContext, DeferredResolver> resolverMap = Maps.newIdentityHashMap();
    private MDeclContext currentContext = null;

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

        //ParentContext = parent Context
        decl.insertData(ParentContext.class, ParentContextData.build(context));

        //Named = declaration name
        name.map(NamedData::build).ifPresent((data) -> decl.insertData(Named.class, data));

        if (type.hasContext == DeclType.HasContext.TRUE) {
            //ContextHolder = own context
            decl.insertData(ContextHolder.class, ContextHolderData.build(
                    context.getOrCreateSubcontext(name, Optional.of(decl)).ref()
            ));
        }

        context.insertDeclaration(decl);

        return decl;
    }

    @Nonnull
    public MDeclContext createTopContext() {
        return new InternalDeclContext(ContextPath.from(""), null, Optional.empty());
    }

    @Nonnull
    public MDeclContext getCurrentContext() {
        return checkNotNull(this.currentContext, "current context not initialized");
    }

    public void setCurrentContext(@Nonnull MDeclContext context) {
        this.currentContext = context;
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

    @Nonnull
    public MDeclContext getDeclContext(ContextPath path) {
        MDeclContext context = currentContext;
        for (String subPath : path.segments()) {
            context = context.getOrCreateSubcontext(Optional.of(subPath), Optional.empty());
        }
        return context;
    }

    @Nonnull
    public DynamicLookup<MDeclaration> deferredLookup(MDeclContext context, String name) {
        return resolver().lookup(context, name);
    }

    public void resolveDeclarations(MDeclContext context) {
        resolverMap.values().forEach((dr) -> dr.resolveWith(context));
    }

    private DeferredResolver resolver() {
        return this.resolverMap.computeIfAbsent(
                checkNotNull(this.currentContext, "current context not initialized"),
                (ignored) -> new DeferredResolver()
        );
    }
}
