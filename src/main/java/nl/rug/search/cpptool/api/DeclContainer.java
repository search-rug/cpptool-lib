package nl.rug.search.cpptool.api;

import com.google.common.base.MoreObjects;
import com.google.common.collect.FluentIterable;

import javax.annotation.Nonnull;

/**
 * This object contains a set of methods to access the data found in the input data-set.
 * <br />
 * There are three sets of data accessible through this class:
 * 1. All the 'includes'-relations used in the original sourcecode.
 * - See {@link #includes()}
 * 2. A declaration tree for each processed source file, containing just the declarations contained in that source-file.
 * - See {@link #inputFiles()} and {@link SourceFile#localContext()}
 * 3. A declaration tree that contains the declarations from all source files as a single tree.
 * - See {@link #context()}
 *
 * @author David van Leusen <J.D.van.leusen@student.rug.nl>
 * @since 2015-06-24
 */
public interface DeclContainer {
    /**
     * @return A top-level global context that contains all the declarations found in the input data-set.
     */
    @Nonnull
    DeclContext context();

    /**
     * @return All source files referenced in the input data-set.
     */
    @Nonnull
    Iterable<SourceFile> allFiles();

    /**
     * @return All source files references in the input set which have emitted declarations.
     * @see {@link SourceFile#localContext()}
     */
    @Nonnull
    default Iterable<SourceFile> inputFiles() {
        return FluentIterable.from(this.allFiles()).filter(file -> file.localContext().isPresent());
    }

    /**
     * Utility method to retrieve pairs describing all #include relations in the emitted source tree.
     * These relations are part of a directed graph, so it is possible for cycles to exist.
     * The output will not contain duplicate entries.
     *
     * @return An iterable collection of all include relations in the original sourcecode.
     */
    @Nonnull
    default Iterable<IncludePair> includes() {
        return FluentIterable.from(this.allFiles()).transformAndConcat(includer ->
                        FluentIterable.from(includer.includes()).transform(includee ->
                                        new IncludePair(includer, includee)
                        )
        );
    }

    class IncludePair {
        public final SourceFile includer;
        public final SourceFile includee;

        private IncludePair(final SourceFile includer, final SourceFile includee) {
            this.includee = includee;
            this.includer = includer;
        }

        @Override
        public String toString() {
            return MoreObjects.toStringHelper(this)
                    .add("includer", includer)
                    .add("includee", includee)
                    .toString();
        }
    }
}
