package nl.rug.search.cpptool.runtime.impl;

import com.google.common.base.MoreObjects;
import com.google.common.base.Strings;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import nl.rug.search.cpptool.api.DeclContext;
import nl.rug.search.cpptool.api.Declaration;
import nl.rug.search.cpptool.api.data.ContextHolder;
import nl.rug.search.cpptool.runtime.mutable.MDeclContext;
import nl.rug.search.cpptool.runtime.mutable.MDeclaration;
import nl.rug.search.cpptool.runtime.util.ContextPath;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.PrintStream;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;
import static nl.rug.search.cpptool.runtime.util.Coerce.coerce;

class InternalDeclContext implements MDeclContext {
    public final static String ANONYMOUS_NAME = "{anonymous}";
    private final RelocatableProperty<MDeclContext> actualRef = RelocatableProperty.wrap(this);
    private final Optional<DeclContext> parent;
    private final Map<String, MDeclContext> children = Maps.newHashMap();
    private final Map<String, DynamicLookup<MDeclaration>> decls = Maps.newHashMap();
    private final List<MDeclContext> anonymous_children = Lists.newLinkedList();
    private final List<DynamicLookup<MDeclaration>> anonymous_decls = Lists.newLinkedList();
    private final ContextPath path;
    private Optional<MDeclaration> decl;

    public InternalDeclContext(@Nonnull ContextPath path, @Nullable DeclContext parent, @Nonnull Optional<MDeclaration> decl) {
        this.path = path;
        this.decl = decl;
        this.parent = Optional.ofNullable(parent);
    }

    @Nonnull
    @Override
    public Iterable<DeclContext> children() {
        return Iterables.unmodifiableIterable(Iterables.concat(
                this.children.values(),
                this.anonymous_children
        ));
    }

    @Nonnull
    @Override
    public Iterable<Declaration> declarations() {
        return Iterables.unmodifiableIterable(Iterables.transform(
                Iterables.concat(this.decls.values(), this.anonymous_decls),
                DynamicLookup::get
        ));
    }

    @Nonnull
    @Override
    public Optional<Declaration> definition() {
        return coerce(this.decl);
    }

    @Nonnull
    @Override
    public DeclContext parent() {
        return this.parent.get();
    }

    @Nonnull
    @Override
    public Optional<String> name() {
        if (!this.parent.isPresent()) { //global context
            return Optional.empty();
        } else {
            return Optional.of(Iterables.getLast(this.path.segments())).filter((n) -> !n.equals(ANONYMOUS_NAME));
        }
    }

    @Override
    public void dump(@Nonnull PrintStream out) {
        dump(checkNotNull(out), 0);
    }

    private void dump(PrintStream out, int indent) {
        final String offset = Strings.repeat("    ", indent);
        out.print(offset);
        out.print("- ");
        if (parent.isPresent()) {
            out.print(decl.map(Object::toString).orElse(name().orElse("{unknown}")));
        } else {
            //Special case for global context
            out.print("{global}");
        }
        out.print('\n');
        for (Declaration decl : declarations()) {
            if (decl.has(ContextHolder.class)) continue;
            out.print(offset);
            out.print("    ");
            out.print("- ");
            out.print(decl);
            out.print('\n');
        }
        for (DeclContext decl : children()) {
            ((InternalDeclContext) decl).dump(out, indent + 1);
        }
    }

    @Override
    public Optional<MDeclaration> getDeclaration(String name) {
        if (this.decls.containsKey(name)) {
            return this.decls.get(name).toOptional();
        } else {
            return Optional.empty();
        }
    }

    @Override
    public MDeclContext getOrCreateSubcontext(Optional<String> name, Optional<MDeclaration> decl) {
        if (name.isPresent() && this.children.containsKey(name.get())) {
            final MDeclContext context = this.children.get(name.get());
            if (!context.definition().isPresent() && decl.isPresent()) {
                context.setDeclaration(decl.get());
            }
            return context;
        } else {
            InternalDeclContext newContext = new InternalDeclContext(
                    this.path.appendPath(name.orElse(ANONYMOUS_NAME)),
                    this,
                    decl
            );
            if (name.isPresent()) {
                this.children.put(name.get(), newContext);
            } else {
                this.anonymous_children.add(newContext);
            }
            return newContext;
        }
    }

    @Override
    public void setDeclaration(MDeclaration decl) {
        checkState(!this.decl.isPresent(), "Declaration has already been set!");
        this.decl = Optional.of(decl);
    }

    @Override
    public void insertDeclaration(MDeclaration decl) {
        final Optional<String> name = decl.name();
        if (name.isPresent()) {
            final String n = name.get();
            //TODO: specializations have the same name...
            //checkState(!decls.containsKey(n), "Double declaration");
            if (decls.containsKey(n)) {
                decls.get(n).get().link(decl.ref());
            } else {
                decls.put(n, decl.ref());
            }
        } else {
            anonymous_decls.add(decl.ref());
        }
    }

    @Override
    public DynamicLookup<MDeclContext> ref() {
        return this.actualRef;
    }

    @Override
    public void setRedirect(MDeclContext redirect) {
        this.actualRef.set(redirect);
    }

    @Override
    public void link(DynamicLookup<MDeclContext> other) {
        this.actualRef.link(other);
    }

    @Nonnull
    @Override
    public ContextPath getPath() {
        return this.path;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper("DeclContext")
                .add("path", path.segments())
                .toString();
    }
}
