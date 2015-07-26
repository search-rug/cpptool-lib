package nl.rug.search.cpptool.runtime.util;

import com.google.common.base.Joiner;
import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import nl.rug.search.cpptool.api.util.ContextTools;
import nl.rug.search.cpptool.proto.Base;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

public class ContextPath {
    private final List<String> pathSegments;

    private ContextPath(final @Nonnull List<String> pathSegments) {
        this.pathSegments = pathSegments;
    }

    @Nonnull
    public static ContextPath from(final @Nonnull String context) {
        return new ContextPath(ContextTools.pathSplitter(context));
    }

    @Nonnull
    public static ContextPath from(final @Nonnull Base.ScopedName name) {
        return ContextPath.from(checkNotNull(name.getContext(), "name == NULL"));
    }

    @Nonnull
    public static String simplify(Base.ScopedName name) {
        checkNotNull(name, "name == NULL");
        if (name.hasContext()) {
            return Joiner.on("::").join(Iterables.concat(
                    ImmutableList.of(""),
                    ContextPath.from(name).segments(),
                    ImmutableList.of(Optional.of(name.getName())
                                    .filter((str) -> !str.trim().isEmpty())
                                    .orElse("{anonymous}")
                    )
            ));
        } else {
            return name.getName();
        }
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("path", Joiner.on("::").join(pathSegments))
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ContextPath that = (ContextPath) o;
        return Objects.equals(pathSegments, that.pathSegments);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pathSegments);
    }

    @Nonnull
    public Iterable<String> segments() {
        return this.pathSegments;
    }

    @Nonnull
    public ContextPath appendPath(String path) {
        return new ContextPath(ImmutableList.<String>builder()
                .addAll(this.pathSegments)
                .add(checkNotNull(path))
                .build());
    }
}
