package nl.rug.search.cpptool.runtime;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import nl.rug.search.cpptool.api.DeclContainer;
import nl.rug.search.cpptool.runtime.impl.ContextFactory;
import nl.rug.search.cpptool.runtime.mutable.MDeclContext;
import nl.rug.search.cpptool.runtime.mutable.MSourceFile;
import nl.rug.search.cpptool.runtime.util.FunctionalCacheLoader;

import java.util.List;
import java.util.Set;

class ResultMerger {
    private final List<PartialResult> partials = Lists.newLinkedList();
    private final Set<String> primaryFiles = Sets.newHashSet();
    private final ContextFactory factory = new ContextFactory();
    private final MDeclContext globalContext = factory.createTopContext();
    private final LoadingCache<String, MSourceFile> fileCache = CacheBuilder.newBuilder()
            .build(new FunctionalCacheLoader<>(factory::createFile));

    public ResultMerger feed(PartialResult result) {
        primaryFiles.add(result.getPrimaryFile());
        partials.add(result);
        return this;
    }

    public DeclContainer build() {
        final Set<String> seenFiles = Sets.newHashSet();

        for (PartialResult result : partials) {
            for (MSourceFile file : result.files()) {
                final MSourceFile outputFile = fileCache.getUnchecked(file.path());
                file.setRedirect(outputFile); //Redirect file

                if (primaryFiles.contains(file.path()) && !result.getPrimaryFile().equals(file.path())) {
                    //This is a primary file, but not the result that has the most information about it, skip.
                    continue;
                } else if (seenFiles.contains(file.path())) {
                    //File has already been added to the output, ignore
                    continue;
                }

                if (file.localContext().isPresent()) {
                    System.out.printf("Merging: %s%n", file.path());
                    mergeDeclarations(
                            globalContext,
                            (MDeclContext) file.localContext().get(),
                            outputFile.getLocalContext(factory::createTopContext)
                    );
                    copyIncludes(file);
                    seenFiles.add(outputFile.path());
                }
            }
        }

        // Copy includes from files that are not part of the input set but might have includes
        FluentIterable.from(partials)
                .transformAndConcat(PartialResult::files)
                .filter((file) -> !seenFiles.contains(file.path()))
                .forEach(this::copyIncludes);

        return factory.build(globalContext, ImmutableList.copyOf(fileCache.asMap().values()));
    }

    private void copyIncludes(MSourceFile originalFile) {
        final MSourceFile file = fileCache.getUnchecked(originalFile.path());
        FluentIterable.from(originalFile.includes())
                .transform((otherFile) -> fileCache.getUnchecked(otherFile.path())) // Map to our files
                .forEach(file::includes); //Add include
    }

    private void mergeDeclarations(MDeclContext globalContext, MDeclContext fileIn, MDeclContext fileOut) {
        // The structure of <fileIn> is replicated in <fileOut> and <globalContext>
    }
}
