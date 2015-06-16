package nl.rug.search.cpptool.runtime.impl;

import com.google.common.base.MoreObjects;
import com.google.common.collect.Sets;
import nl.rug.search.cpptool.api.DeclContext;
import nl.rug.search.cpptool.api.SourceFile;
import nl.rug.search.cpptool.runtime.mutable.MDeclContext;
import nl.rug.search.cpptool.runtime.mutable.MSourceFile;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

import static nl.rug.search.cpptool.runtime.util.Coerce.coerce;

class InternalSourceFile implements MSourceFile {
    private final String path;
    private final Set<MSourceFile> includes = Sets.newHashSet();
    private final Set<MSourceFile> includedBy = Sets.newHashSet();
    private Optional<MDeclContext> context = Optional.empty();

    public InternalSourceFile(String path) {
        this.path = path;
    }

    @Nonnull
    @Override
    public String path() {
        return this.path;
    }

    @Nonnull
    @Override
    public Iterable<SourceFile> includes() {
        return Collections.unmodifiableSet(includes);
    }

    @Nonnull
    @Override
    public Iterable<SourceFile> includedBy() {
        return Collections.unmodifiableSet(includedBy);
    }

    @Nonnull
    @Override
    public Optional<DeclContext> localContext() {
        return coerce(this.context);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InternalSourceFile that = (InternalSourceFile) o;
        return Objects.equals(path, that.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(path);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper("SourceFile")
                .add("path", path)
                .toString();
    }

    @Nonnull
    @Override
    public MDeclContext getLocalContext(Supplier<MDeclContext> contextSupplier) {
        if (this.context.isPresent()) {
            return this.context.get();
        } else {
            final MDeclContext newContext = contextSupplier.get();
            this.context = Optional.of(newContext);
            return newContext;
        }
    }

    @Override
    public void addIncludedBy(MSourceFile file) {
        this.includedBy.add(file);
    }

    @Override
    public void addIncludes(MSourceFile file) {
        this.includes.add(file);
    }
}
