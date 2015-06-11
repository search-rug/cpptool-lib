package nl.rug.search.cpptool.api;

import com.google.common.collect.ImmutableList;
import nl.rug.search.cpptool.api.util.ContextTools;

import javax.annotation.Nonnull;
import java.util.Optional;

public interface SourceFile {
    Iterable<Declaration> MISSING_DECLARATIONS_DUMMY = ImmutableList.of();

    /**
     * @return
     */
    @Nonnull
    String path();

    /**
     * @return
     */
    @Nonnull
    Iterable<SourceFile> includes();

    /**
     * @return
     */
    @Nonnull
    Iterable<SourceFile> includedBy();

    /**
     * @return
     */
    @Nonnull
    Optional<DeclContext> localContext();

    /**
     * @return
     */
    @Nonnull
    default Iterable<Declaration> declarations() {
        return localContext().map(ContextTools::traverseDecls).orElse(MISSING_DECLARATIONS_DUMMY);
    }
}
