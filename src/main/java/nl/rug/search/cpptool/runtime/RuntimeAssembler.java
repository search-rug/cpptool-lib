package nl.rug.search.cpptool.runtime;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.common.util.concurrent.*;
import nl.rug.search.cpptool.api.DeclContainer;
import nl.rug.search.cpptool.api.io.Assembler;

import javax.annotation.Nonnull;
import java.io.File;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

public class RuntimeAssembler implements Assembler {
    private final AtomicBoolean built = new AtomicBoolean(false);
    private final ListeningExecutorService executor;
    private final List<ListenableFuture<PartialResult>> results = Lists.newLinkedList();
    private final Set<String> addedFiles = Sets.newHashSet();
    private ListenableFuture<DeclContainer> finalResult = null;

    public RuntimeAssembler(final @Nonnull ExecutorService executor) {
        this.executor = MoreExecutors.listeningDecorator(checkNotNull(executor, "executor == NULL"));
    }

    @Nonnull
    @Override
    public Assembler read(@Nonnull File input) {
        checkState(!built.get(), "Assembler.deferBuild has already been invoked");
        checkNotNull(input, "input == NULL");

        final String filePath = input.getAbsolutePath();
        if (!addedFiles.contains(filePath)) {
            results.add(executor.submit(new PartialResultBuilder(input)));
            addedFiles.add(filePath);
        }

        return this;
    }

    @Nonnull
    @Override
    public synchronized ListenableFuture<DeclContainer> deferBuild() {
        if (built.get()) {
            checkState(this.finalResult != null, "Assembler.deferBuild called but result is missing.");
            return this.finalResult;
        } else {
            this.finalResult = Futures.transform(
                    Futures.allAsList(this.results),
                    this::mergeResults,
                    executor
            );
            // Cleanup callback
            Futures.addCallback(this.finalResult, new FutureCallback<DeclContainer>() {
                @Override
                public void onSuccess(DeclContainer result) {
                    cleanup();
                }

                private void cleanup() {
                    results.clear();
                    executor.shutdown();
                }

                @Override
                public void onFailure(Throwable t) {
                    cleanup();
                }
            }, executor);
            built.set(true);
            return this.finalResult;
        }
    }

    private DeclContainer mergeResults(List<PartialResult> results) {
        results.forEach(Preconditions::checkNotNull); //Sanity check

        final ResultMerger merger = new ResultMerger();
        results.forEach(merger::feed);
        return merger.build();
    }
}
