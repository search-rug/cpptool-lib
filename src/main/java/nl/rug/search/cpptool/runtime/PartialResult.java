package nl.rug.search.cpptool.runtime;

import com.google.common.base.MoreObjects;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.LoadingCache;
import nl.rug.search.cpptool.api.DeclContainer;
import nl.rug.search.cpptool.api.DeclType;
import nl.rug.search.cpptool.api.data.Named;
import nl.rug.search.cpptool.runtime.data.NamedData;
import nl.rug.search.cpptool.runtime.impl.ContextFactory;
import nl.rug.search.cpptool.runtime.impl.DynamicLookup;
import nl.rug.search.cpptool.runtime.impl.LookupRegistry;
import nl.rug.search.cpptool.runtime.impl.RelocatableProperty;
import nl.rug.search.cpptool.runtime.mutable.MDeclContext;
import nl.rug.search.cpptool.runtime.mutable.MDeclaration;
import nl.rug.search.cpptool.runtime.mutable.MSourceFile;
import nl.rug.search.cpptool.runtime.mutable.MType;
import nl.rug.search.cpptool.runtime.processor.BuilderContext;
import nl.rug.search.cpptool.runtime.util.FunctionalCacheLoader;
import nl.rug.search.proto.Base;

import javax.annotation.Nonnull;
import java.util.Optional;

class PartialResult implements BuilderContext {
    private final ContextFactory contextFactory = new ContextFactory();
    private final LookupRegistry lookup = new LookupRegistry(this, this.contextFactory);
    private final LoadingCache<String, RelocatableProperty<MSourceFile>> fileCache = CacheBuilder.newBuilder()
            .build(new FunctionalCacheLoader<>(path -> {
                final RelocatableProperty<MSourceFile> relocatable = new RelocatableProperty<>();
                relocatable.set(contextFactory.createFile(path));
                return relocatable;
            }));
    private final DynamicLookup<MSourceFile> targetFile;

    public PartialResult(String targetFile) {
        this.targetFile = file(targetFile);
    }

    @Nonnull
    @Override
    public MDeclaration createDeclaration(@Nonnull Base.ScopedName name, @Nonnull DeclType type) {
        final MDeclContext context = this.lookup.declContexts().lookup(name);
        final MDeclaration decl = this.contextFactory.createDeclaration(context, type, Optional.of(name.getName()));
        decl.insertData(Named.class, NamedData.build(name.getName()));
        return decl;
    }

    @Nonnull
    @Override
    public MDeclaration createIsolatedContext(@Nonnull Base.IsolatedContextDefinition contextDefinition) {
        final MDeclContext context = this.lookup.declContexts().lookup(contextDefinition);
        final MDeclaration decl = this.contextFactory.createDeclaration(context, DeclType.ISOLATED_CONTEXT);
        this.lookup.declContexts().registerIsolatedContext(
                contextDefinition.getContextId(),
                (MDeclContext) decl.dataUnchecked(DeclContainer.class).context()
        );
        return decl;
    }

    @Nonnull
    @Override
    public DynamicLookup<MType> findType(@Nonnull Base.Type type) {
        //TODO: modifiers...
        return this.lookup.types().lookup(type);
    }

    @Nonnull
    @Override
    public DynamicLookup<MSourceFile> file(@Nonnull String filePath) {
        return this.fileCache.getUnchecked(filePath);
    }

    @Nonnull
    @Override
    public DynamicLookup<MDeclaration> findDeclaration(@Nonnull Base.ScopedName name) {
        return this.lookup.decls().lookup(name);
    }

    @Nonnull
    @Override
    public DynamicLookup<MDeclaration> findDeclaration(@Nonnull Base.Type type) {
        return this.lookup.decls().lookup(type);
    }

    @Override
    public void switchInputFile(@Nonnull String filePath) {
        this.contextFactory.setContextMirror(
                file(filePath).get().getLocalContext(this.contextFactory::createTopContext)
        );
    }

    @Override
    public void createTypeMapping(@Nonnull Base.TypeDefinition typeDefinition) {
        this.lookup.types().define(typeDefinition);
    }

    @Override
    public void updateTypeMapping(@Nonnull Base.Type type, @Nonnull MDeclaration decl) {
        this.lookup.types().update(type, decl);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("targetFile", targetFile)
                .toString();
    }
}
