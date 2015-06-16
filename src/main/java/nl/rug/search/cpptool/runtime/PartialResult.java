package nl.rug.search.cpptool.runtime;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Maps;
import nl.rug.search.cpptool.api.DeclContainer;
import nl.rug.search.cpptool.api.DeclType;
import nl.rug.search.cpptool.api.data.Named;
import nl.rug.search.cpptool.runtime.data.NamedData;
import nl.rug.search.cpptool.runtime.impl.ContextFactory;
import nl.rug.search.cpptool.runtime.impl.DynamicLookup;
import nl.rug.search.cpptool.runtime.impl.RelocatableProperty;
import nl.rug.search.cpptool.runtime.mutable.MDeclContext;
import nl.rug.search.cpptool.runtime.mutable.MDeclaration;
import nl.rug.search.cpptool.runtime.mutable.MSourceFile;
import nl.rug.search.cpptool.runtime.mutable.MType;
import nl.rug.search.cpptool.runtime.processor.BuilderContext;
import nl.rug.search.cpptool.runtime.util.FunctionalCacheLoader;
import nl.rug.search.proto.Base;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.Optional;

import static nl.rug.search.cpptool.runtime.util.Debug.NYI;

class PartialResult implements BuilderContext {
    private final ContextFactory contextFactory = new ContextFactory();
    private final Map<Long, MDeclContext> isolatedContexts = Maps.newHashMap();
    private final Map<Long, RelocatableProperty<MType>> typeMapping = Maps.newHashMap();
    private final LoadingCache<String, RelocatableProperty<MSourceFile>> fileCache = CacheBuilder.newBuilder()
            .build(new FunctionalCacheLoader<>(path -> {
                final RelocatableProperty<MSourceFile> relocatable = new RelocatableProperty<>();
                relocatable.set(contextFactory.newFile(path));
                return relocatable;
            }));

    public PartialResult(String targetFile) {

    }

    @Nonnull
    @Override
    public MDeclaration createDeclaration(@Nonnull Base.ScopedName name, @Nonnull DeclType type) {
        //TODO: good way of accessing context
        final MDeclaration decl = this.contextFactory.createDeclaration(null, type, Optional.of(name.getName()));
        decl.insertData(Named.class, NamedData.build(name.getName()));
        return decl;
    }

    @Nonnull
    @Override
    public MDeclaration createIsolatedContext(@Nonnull Base.IsolatedContextDefinition contextDefinition) {
        //TODO: find a way to get the parent context
        final MDeclaration decl = this.contextFactory.createDeclaration(null, DeclType.ISOLATED_CONTEXT);
        isolatedContexts.put(
                contextDefinition.getContextId(),
                (MDeclContext) decl.dataUnchecked(DeclContainer.class).context()
        );
        return decl;
    }

    @Nonnull
    @Override
    public DynamicLookup<MType> findType(@Nonnull Base.Type type) {
        //TODO: modifiers...
        return this.typeMapping.get(type.getTypeId());
    }

    @Nonnull
    @Override
    public DynamicLookup<MSourceFile> file(@Nonnull String filePath) {
        return this.fileCache.getUnchecked(filePath);
    }

    @Nonnull
    @Override
    public DynamicLookup<MDeclaration> findDeclaration(@Nonnull Base.ScopedName name) {
        throw NYI();
    }

    @Nonnull
    @Override
    public DynamicLookup<MDeclaration> findDeclaration(@Nonnull Base.Type type) {
        throw NYI();
    }

    @Override
    public void switchInputFile(@Nonnull String filePath) {
        this.contextFactory.setContextMirror(
                file(filePath).get().getLocalContext(this.contextFactory::createTopContext)
        );
    }

    @Override
    public void createTypeMapping(@Nonnull Base.TypeDefinition typeDefinition) {
        throw NYI();
    }
}
