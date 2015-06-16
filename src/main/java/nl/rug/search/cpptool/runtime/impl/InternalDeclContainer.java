package nl.rug.search.cpptool.runtime.impl;

import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableList;
import nl.rug.search.cpptool.api.DeclContainer;
import nl.rug.search.cpptool.api.DeclContext;
import nl.rug.search.cpptool.api.SourceFile;

import javax.annotation.Nonnull;
import java.util.List;

class InternalDeclContainer implements DeclContainer {
    private final DeclContext topContext;
    private final List<SourceFile> sourceFiles;

    public InternalDeclContainer(DeclContext topContext, List<SourceFile> sourceFiles) {
        this.topContext = topContext;
        this.sourceFiles = ImmutableList.copyOf(sourceFiles);
    }

    @Nonnull
    @Override
    public DeclContext context() {
        return this.topContext;
    }

    @Nonnull
    @Override
    public Iterable<SourceFile> allFiles() {
        return this.sourceFiles;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper("DeclContainer")
                .add("sourceFiles", sourceFiles)
                .toString();
    }
}
