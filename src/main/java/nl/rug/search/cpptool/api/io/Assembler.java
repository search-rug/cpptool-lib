package nl.rug.search.cpptool.api.io;

import com.google.common.base.Throwables;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.MoreExecutors;
import nl.rug.search.cpptool.api.DeclContainer;
import nl.rug.search.cpptool.runtime.RuntimeAssembler;

import javax.annotation.Nonnull;
import java.io.File;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;

public interface Assembler {
    @Nonnull
    static Assembler create(final @Nonnull ExecutorService executor) {
        return new RuntimeAssembler(executor);
    }

    @Nonnull
    static Assembler create() {
        return create(MoreExecutors.newDirectExecutorService());
    }

    @Nonnull
    Assembler read(final @Nonnull File input);

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
