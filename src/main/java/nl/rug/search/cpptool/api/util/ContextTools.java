package nl.rug.search.cpptool.api.util;

import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableList;
import nl.rug.search.cpptool.api.DeclContext;
import nl.rug.search.cpptool.api.Declaration;

import javax.annotation.Nonnull;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

public interface ContextTools {
    @Nonnull
    static Iterable<Declaration> traverseDecls(final @Nonnull DeclContext context) {
        checkNotNull(context, "context == NULL");
        return FluentIterable.from(context.children())
                .transformAndConcat(ContextTools::traverseDecls) // Recursively get and concat all child declarations
                .append(context.declarations()); // Append own declarations
    }

    @Nonnull
    static Iterable<Declaration> validateState(final @Nonnull DeclContext context) {
        return FluentIterable.from(traverseDecls(context)).filter(Declaration::validateState);
    }

    @Nonnull
    static List<String> pathSplitter(final @Nonnull String path) {
        checkNotNull(path, "path == NULL");
        ImmutableList.Builder<String> builder = ImmutableList.builder();
        final char[] chars = path.toCharArray();
        final int len = chars.length;
        int previousCut = 0;
        for (int i = 0; i < len; ) {
            if (chars[i] == ':') {
                if (i + 1 < len && chars[i + 1] == ':') {
                    if (previousCut < i) {
                        builder.add(path.substring(previousCut, i));
                    }
                    i += 2;
                    previousCut = i;
                    continue;
                }
            } else if (chars[i] == '(') {
                final int closingBrace = path.indexOf(')', i + 1);
                checkState(closingBrace != -1, "Invalid path");
                i = closingBrace + 1;
                continue;
            }

            i++;
        }

        if (previousCut < len) {
            builder.add(path.substring(previousCut));
        }

        return builder.build();
    }
}
