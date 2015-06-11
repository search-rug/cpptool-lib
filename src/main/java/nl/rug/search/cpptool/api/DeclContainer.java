package nl.rug.search.cpptool.api;

import com.google.common.base.MoreObjects;
import com.google.common.collect.FluentIterable;

import javax.annotation.Nonnull;

/**
 *
 */
public interface DeclContainer {
    /**
     * @return
     */
    @Nonnull
    DeclContext context();

    /**
     * @return
     */
    @Nonnull
    Iterable<SourceFile> allFiles();

    /**
     * @return
     */
    @Nonnull
    default Iterable<SourceFile> inputFiles() {
        return FluentIterable.from(this.allFiles()).filter(file -> file.localContext().isPresent());
    }

    /**
     * @return
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
