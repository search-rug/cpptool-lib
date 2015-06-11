package nl.rug.search.cpptool.api.util;

import nl.rug.search.cpptool.api.Declaration;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

public class MissingDataException extends RuntimeException {

    public MissingDataException(final @Nonnull Declaration decl, final @Nonnull Class<?> dataType) {
        super(buildMessage(decl, dataType));
    }

    @Nonnull
    public static Supplier<MissingDataException> supplier(
            final @Nonnull Declaration decl,
            final @Nonnull Class<?> dataType
    ) {
        //noinspection ThrowableInstanceNeverThrown
        return () -> new MissingDataException(decl, dataType);
    }

    @Nonnull
    private static String buildMessage(final @Nonnull Declaration decl, final @Nonnull Class<?> dataType) {
        return String.format(
                "MissingData{decl=%s,data=%s,consistent=%b}",
                decl,
                dataType.getSimpleName(),
                decl.type().check(decl)
        );
    }
}
