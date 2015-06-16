package nl.rug.search.cpptool.runtime.impl;

import com.google.common.collect.Maps;
import nl.rug.search.cpptool.api.data.Location;
import nl.rug.search.cpptool.api.util.ContextTools;
import nl.rug.search.cpptool.runtime.mutable.MDeclContext;
import nl.rug.search.cpptool.runtime.mutable.MDeclaration;
import nl.rug.search.cpptool.runtime.mutable.MType;
import nl.rug.search.cpptool.runtime.processor.BuilderContext;
import nl.rug.search.proto.Base;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.Optional;

import static nl.rug.search.cpptool.runtime.util.Coerce.coerce;

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
                return contextFactory.getDeclContext(ContextTools.pathSplitter(name.getContext()));
            }
        }
    }

    public class Types {
        private final Map<Long, RelocatableProperty<MType>> typeMap = Maps.newHashMap();

        @Nonnull
        public DynamicLookup<MType> lookup(final @Nonnull Base.Type type) {
            return coerce(typeMap.get(type.getTypeId()));
        }

        public void define(Base.TypeDefinition definition) {
            final Base.ScopedName name = definition.getSpecifier();
            final Optional<Location> loc = context.toLocation(
                    definition.getLocation(),
                    definition.hasLocation()
            );

            //Build type
            final RelocatableProperty<MType> type = new RelocatableProperty<>();
            type.set(contextFactory.createType(name, loc, definition.getStronglyDefined()));

            //Make type available for lookup
            this.typeMap.put(definition.getTypeId(), type);
        }

        public void update(Base.Type type, MDeclaration decl) {
            this.typeMap.get(type.getTypeId()).get().updateDeclaration(decl);
        }
    }

    public class Declarations {
        @Nonnull
        public DynamicLookup<MDeclaration> lookup(final @Nonnull Base.Type type) {
            return types().lookup(type).get().getDeclaration();
        }

        @Nonnull
        public DynamicLookup<MDeclaration> lookup(final @Nonnull Base.ScopedName name) {
            return declContexts().lookup(name).getDeclaration(name.getName());
        }
    }
}
