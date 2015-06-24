package nl.rug.search.cpptool.api.data;

import nl.rug.search.cpptool.api.SourceFile;

import javax.annotation.Nonnull;

/**
 * Data component that specifies the location in which the declaration is declared.
 * This location is based on the source before any pre-processing or parsing.
 *
 * @author David van Leusen <J.D.van.leusen@student.rug.nl>
 * @since 2015-06-24
 */
public interface Location {
    @Nonnull
    SourceFile file();

    @Nonnull
    default String filePath() {
        return file().path();
    }

    @Nonnull
    Cursor start();

    @Nonnull
    Cursor end();

    interface Cursor {
        int line();

        int column();
    }
}
