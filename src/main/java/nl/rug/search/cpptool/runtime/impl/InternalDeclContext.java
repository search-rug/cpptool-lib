package nl.rug.search.cpptool.runtime.impl;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import nl.rug.search.cpptool.api.DeclContext;
import nl.rug.search.cpptool.api.Declaration;
import nl.rug.search.cpptool.runtime.mutable.MDeclContext;
import nl.rug.search.cpptool.runtime.mutable.MDeclaration;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.PrintStream;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;
import static nl.rug.search.cpptool.runtime.util.Coerce.coerce;
import static nl.rug.search.cpptool.runtime.util.Debug.NYI;

class InternalDeclContext implements MDeclContext {
    private final Optional<DeclContext> parent;
    private Optional<Map<String, ? extends MDeclContext>> children = Optional.empty();
    private Optional<Map<String, ? extends MDeclaration>> decls = Optional.empty();
    private Optional<List<? extends MDeclContext>> anonymous_children = Optional.empty();

    public InternalDeclContext(@Nullable DeclContext parent) {
        this.parent = Optional.ofNullable(parent);
    }

    @Nonnull
    @Override
    public Iterable<DeclContext> children() {
        return Iterables.concat(
                children.map(Map::values).orElse(ImmutableList.of()),
                anonymous_children.orElse(ImmutableList.of())
        );
    }

    @Nonnull
    @Override
    public Iterable<Declaration> declarations() {
        return coerce(decls.map(Map::values).orElse(ImmutableList.of()));
    }

    @Nonnull
    @Override
    public Optional<Declaration> definition() {
        throw NYI();
    }

    @Nonnull
    @Override
    public DeclContext parent() {
        return this.parent.get();
    }

    @Nonnull
    @Override
    public Optional<String> name() {
        throw NYI();
    }

    @Override
    public void dump(@Nonnull PrintStream out) {
        dump(checkNotNull(out), 0);
    }

    private void dump(PrintStream out, int indent) {
        throw NYI();
    }
}