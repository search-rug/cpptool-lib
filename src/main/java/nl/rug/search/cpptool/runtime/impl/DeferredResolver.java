package nl.rug.search.cpptool.runtime.impl;

import com.google.common.base.Predicates;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import nl.rug.search.cpptool.runtime.mutable.MDeclContext;
import nl.rug.search.cpptool.runtime.mutable.MDeclaration;
import nl.rug.search.cpptool.runtime.util.ContextPath;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkState;

public class DeferredResolver {
    private final Map<DeclKey, RelocatableProperty<MDeclaration>> deferredLookups = Maps.newHashMap();

    public void resolveWith(final MDeclContext context) {
        final List<DeclKey> resolvedKeys = FluentIterable.from(deferredLookups.entrySet()).filter((e) -> {
            //Try to find declaration
            final Optional<MDeclaration> decl = tryLookup(e.getKey(), context);

            //Update declaration if its found
            decl.ifPresent((d) -> e.getValue().set(d));

            //Did we update it?
            return decl.isPresent();
        }).transform(Map.Entry::getKey).toList();

        // Remove entries that we have resolved
        resolvedKeys.forEach(deferredLookups::remove);
        //System.out.printf("%d resolved, %d unresolved%n", resolvedKeys.size(), deferredLookups.size());
    }

    private Optional<MDeclaration> tryLookup(DeclKey key, final MDeclContext context) {
        MDeclContext contextIt = context;
        for (String segment : key.path.segments()) {
            contextIt = contextIt.getOrCreateSubcontext(Optional.of(segment), Optional.empty());
        }
        return contextIt.getDeclaration(key.name);
    }

    @Nonnull
    public DynamicLookup<MDeclaration> lookup(MDeclContext context, String name) {
        final Optional<MDeclaration> optDecl = context.getDeclaration(name);
        if (optDecl.isPresent()) {
            return optDecl.get().ref();
        } else {
            return find(context.getPath(), name);
        }
    }

    @Nonnull
    private DynamicLookup<MDeclaration> find(ContextPath path, String name) {
        checkState(Iterables.all(
                path.segments(),
                Predicates.not((segment) -> segment.equals(InternalDeclContext.ANONYMOUS_NAME))
        ), "Unable to find declaration in isolated context");

        final DeclKey key = DeclKey.create(path, name);
        return this.deferredLookups.computeIfAbsent(key, (ignored) -> RelocatableProperty.empty());
    }

    /**
     * Utility class to wrap a [Context,Name] pair and provide a container-friendly key format.
     */
    private static class DeclKey {
        public final ContextPath path;
        public final String name;

        private DeclKey(ContextPath path, String name) {
            this.path = path;
            this.name = name;
        }

        public static DeclKey create(ContextPath path, String name) {
            return new DeclKey(path, name);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            DeclKey declKey = (DeclKey) o;
            return Objects.equals(path, declKey.path) &&
                    Objects.equals(name, declKey.name);
        }

        @Override
        public String toString() {
            return Iterables.toString(Iterables.concat(path.segments(), ImmutableList.of(name)));
        }

        @Override
        public int hashCode() {
            return Objects.hash(path, name);
        }
    }
}
