package nl.rug.search.cpptool.runtime;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import nl.rug.search.cpptool.api.DeclContainer;
import nl.rug.search.cpptool.api.io.Assembler;

import javax.annotation.Nonnull;
import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;
import static nl.rug.search.cpptool.runtime.util.Debug.NYI;

public class RuntimeAssembler implements Assembler {
    private final AtomicBoolean built = new AtomicBoolean(false);
    private final ListeningExecutorService executor;
    private final List<ListenableFuture<PartialResult>> results = Lists.newLinkedList();

    public RuntimeAssembler(final @Nonnull ExecutorService executor) {
        this.executor = MoreExecutors.listeningDecorator(checkNotNull(executor, "executor == NULL"));
    }

    @Nonnull
    @Override
    public Assembler read(@Nonnull File input) {
        checkState(!built.get(), "Assembler.deferBuild has already been invoked");
        results.add(executor.submit(new PartialResultBuilder(checkNotNull(input, "input == NULL"))));
        return this;
    }

    @Nonnull
    @Override
    public synchronized ListenableFuture<DeclContainer> deferBuild() {
        checkState(!built.get(), "Assembler.deferBuild has already been invoked");
        built.set(true);
        return Futures.transform(Futures.allAsList(results), this::mergeResults, executor);
    }

    private DeclContainer mergeResults(List<PartialResult> results) {
        results.forEach(Preconditions::checkNotNull); //Sanity check
        throw NYI();
    }
}
