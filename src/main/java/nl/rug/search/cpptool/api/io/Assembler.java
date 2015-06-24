package nl.rug.search.cpptool.api.io;

import com.google.common.base.Throwables;
import com.google.common.util.concurrent.ListenableFuture;
import nl.rug.search.cpptool.api.DeclContainer;
import nl.rug.search.cpptool.runtime.RuntimeAssembler;

import javax.annotation.Nonnull;
import java.io.File;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * The assembler takes a set of CppTool .pb input files and asynchronously reads and merges the data they contain into
 * a single declaration tree.
 *
 * @author David van Leusen <J.D.van.leusen@student.rug.nl>
 * @since 2015-06-24
 */
public interface Assembler {
    @Nonnull
    static Assembler create(final @Nonnull ExecutorService executor) {
        return new RuntimeAssembler(executor);
    }

    @Nonnull
    static Assembler create() {
        return create(Executors.newSingleThreadExecutor());
    }

    /**
     * Adds the given {@link File} to the input for the declaration tree builder.
     * <br />
     * Duplicate input (same file path) will be ignored.
     *
     * @param input CppTool .pb data file
     * @return this object for method chaining
     * @throws IllegalStateException if called after calling {@link #deferBuild()}
     */
    @Nonnull
    Assembler read(final @Nonnull File input);

    /**
     * Signal the assembler to finalize the input and begin building the declaration tree.
     * Once this method has been called, it is no longer possible to add input to the assembler.
     *
     * @return A future of the declaration container.
     */
    @Nonnull
    ListenableFuture<DeclContainer> deferBuild();

    @Nonnull
    default DeclContainer build() throws InterruptedException {
        try {
            return deferBuild().get();
        } catch (ExecutionException e) {
            throw Throwables.propagate(e.getCause());
        }
    }
}
