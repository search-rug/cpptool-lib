package nl.rug.search.cpptool.runtime.mutable;

import nl.rug.search.cpptool.api.SourceFile;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

public interface MSourceFile extends SourceFile {
    @Nonnull
    MDeclContext getLocalContext(Supplier<MDeclContext> contextSupplier);

    void addIncludedBy(MSourceFile file);

    void addIncludes(MSourceFile file);

    default void includes(MSourceFile file) {
        this.addIncludes(file);
        file.addIncludes(this);
    }
}
