package nl.rug.search.cpptool.runtime;

import com.google.common.base.MoreObjects;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Lists;
import nl.rug.search.cpptool.api.DeclType;
import nl.rug.search.cpptool.api.data.ContextHolder;
import nl.rug.search.cpptool.runtime.impl.ContextFactory;
import nl.rug.search.cpptool.runtime.impl.DynamicLookup;
import nl.rug.search.cpptool.runtime.impl.LookupRegistry;
import nl.rug.search.cpptool.runtime.mutable.MDeclContext;
import nl.rug.search.cpptool.runtime.mutable.MDeclaration;
import nl.rug.search.cpptool.runtime.mutable.MSourceFile;
import nl.rug.search.cpptool.runtime.mutable.MType;
import nl.rug.search.cpptool.runtime.processor.BuilderContext;
import nl.rug.search.cpptool.runtime.util.FunctionalCacheLoader;
import nl.rug.search.proto.Base;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

class PartialResult implements BuilderContext {
    private final ContextFactory contextFactory = new ContextFactory();
    private final LookupRegistry lookup = new LookupRegistry(this, this.contextFactory);
    private final LoadingCache<String, MSourceFile> fileCache = CacheBuilder.newBuilder()
            .build(new FunctionalCacheLoader<>(contextFactory::createFile));
    private final List<Runnable> deferredActions = Lists.newLinkedList();
    private final String targetFile;

    public PartialResult(String targetFile) {
        this.targetFile = targetFile;
    }

    @Nonnull
    @Override
    public MDeclaration createDeclaration(@Nonnull Base.ScopedName name, @Nonnull DeclType type) {
        final MDeclContext context = this.lookup.declContexts().lookup(name);
        return this.contextFactory.createDeclaration(context, type, Optional.of(name.getName()));
    }

    @Nonnull
    @Override
    public MDeclaration createIsolatedContext(@Nonnull Base.IsolatedContextDefinition contextDefinition) {
        final MDeclContext context = this.lookup.declContexts().lookup(contextDefinition);
        final MDeclaration decl = this.contextFactory.createDeclaration(context, DeclType.ISOLATED_CONTEXT);
        this.lookup.declContexts().registerIsolatedContext(
                contextDefinition.getContextId(),
                (MDeclContext) decl.dataUnchecked(ContextHolder.class).context()
        );
        this.lookup.types().resolveIsoContextType(contextDefinition.getOwnType(), decl);
        return decl;
    }

    @Nonnull
    @Override
    public MType findType(@Nonnull Base.Type type) {
        //TODO: modifiers...
        return this.lookup.types().lookup(type);
    }

    @Nonnull
    @Override
    public DynamicLookup<MSourceFile> file(@Nonnull String filePath) {
        return this.fileCache.getUnchecked(filePath).ref();
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
        this.contextFactory.setCurrentContext(
                file(filePath).get().getLocalContext(this.contextFactory::createTopContext)
        );
    }

    @Override
    public void createTypeMapping(@Nonnull Base.TypeDefinition typeDefinition) {
        this.lookup.types().define(typeDefinition);
    }

    @Override
    public void updateTypeMapping(@Nonnull Base.Type type, @Nonnull MDeclaration decl) {
        //TODO: This probably needs to be done through declarations...
        //this.lookup.types().update(type, decl);
    }

    @Override
    public void defer(@Nonnull Runnable deferrable) {
        this.deferredActions.add(checkNotNull(deferrable));
    }

    public void performDeferredActions() {
        //At the moment the only deferred definitions are templates
        //Since parameters are somehow emitted before the templates, we need to reverse
        //this list so templates are defined before parameters.
        Lists.reverse(this.deferredActions).forEach(Runnable::run);
        this.deferredActions.clear();
    }

    public void resolveDeclarations(MDeclContext context) {
        this.contextFactory.resolveDeclarations(context);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("targetFile", targetFile)
                .toString();
    }

    @Nonnull
    public String getPrimaryFile() {
        return this.targetFile;
    }

    public Iterable<MSourceFile> files() {
        return this.fileCache.asMap().values();
    }
}
