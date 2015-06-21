package nl.rug.search.cpptool.runtime.impl;

import com.google.common.collect.Maps;
import nl.rug.search.cpptool.api.data.Location;
import nl.rug.search.cpptool.runtime.mutable.MDeclContext;
import nl.rug.search.cpptool.runtime.mutable.MDeclaration;
import nl.rug.search.cpptool.runtime.mutable.MType;
import nl.rug.search.cpptool.runtime.processor.BuilderContext;
import nl.rug.search.cpptool.runtime.util.ContextPath;
import nl.rug.search.proto.Base;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.Optional;

public class LookupRegistry {
    private final DeclContexts contexts = new DeclContexts();
    private final Types types = new Types();
    private final Declarations decls = new Declarations();
    private final BuilderContext context;
    private final ContextFactory contextFactory;

    public LookupRegistry(BuilderContext context, ContextFactory contextFactory) {
        this.context = context;
        this.contextFactory = contextFactory;
    }

    @Nonnull
    public DeclContexts declContexts() {
        return this.contexts;
    }

    @Nonnull
    public Types types() {
        return this.types;
    }

    @Nonnull
    public Declarations decls() {
        return this.decls;
    }

    public class DeclContexts {
        private final Map<Long, MDeclContext> isolatedContexts = Maps.newHashMap();

        public void registerIsolatedContext(long contextId, MDeclContext context) {
            this.isolatedContexts.put(contextId, context);
        }

        @Nonnull
        public MDeclContext lookup(Base.IsolatedContextDefinition isoContext) {
            //uses isoContext.getParent() for lookups
            //TODO: is isoContext.getParent all we need to find the parent?
            //TODO: assuming getParent().getName() is ignored...
            return lookup(isoContext.getParent());
        }

        @Nonnull
        public MDeclContext lookup(Base.ScopedName name) {
            //use name.context or name.isolated_context_id to find the context
            if (name.hasIsolatedContextId()) {
                //TODO: if isolated_context_id is set, is this actually the context?
                //TODO: or is there a higher context
                return this.isolatedContexts.get(name.getIsolatedContextId());
            } else {
                return contextFactory.getDeclContext(ContextPath.from(name));
            }
        }
    }

    public class Types {
        private final Map<Long, MType> typeMap = Maps.newHashMap();
        private final Map<Long, RelocatableProperty<MDeclaration>> isoContextsDeferred = Maps.newHashMap();

        @Nonnull
        public MType lookup(final @Nonnull Base.Type type) {
            return typeMap.get(type.getTypeId());
        }

        public void define(Base.TypeDefinition definition) {
            final Base.ScopedName name = definition.getSpecifier();
            final Optional<Location> loc = context.toLocation(
                    definition.getLocation(),
                    definition.hasLocation()
            );

            //Make type available for lookup
            this.typeMap.put(
                    definition.getTypeId(),
                    contextFactory.createType(
                            name,
                            loc,
                            definition.getStronglyDefined(),
                            findDecl(name, definition.getTypeId())
                    )
            );
        }

        private DynamicLookup<MDeclaration> findDecl(Base.ScopedName name, long typeId) {
            if (name.getName().trim().isEmpty()) {
                // Isolated context type, they have no name.
                // Defer definition to {@see #resolveIsoContextType}
                return isoContextsDeferred.computeIfAbsent(typeId, (ignored) -> RelocatableProperty.empty());
            } else {
                return decls().lookup(name);
            }
        }

        public void resolveIsoContextType(Base.Type ownType, MDeclaration decl) {
            isoContextsDeferred.get(ownType.getTypeId()).set(decl);
        }
    }

    public class Declarations {
        @Nonnull
        public DynamicLookup<MDeclaration> lookup(final @Nonnull Base.Type type) {
            return types().lookup(type).decl();
        }

        @Nonnull
        public DynamicLookup<MDeclaration> lookup(final @Nonnull Base.ScopedName name) {
            if (name.hasContext()) {
                return contextFactory.deferredLookup(declContexts().lookup(name), name.getName());
            } else {
                //Builtin
                return RelocatableProperty.empty();
            }
        }
    }
}
