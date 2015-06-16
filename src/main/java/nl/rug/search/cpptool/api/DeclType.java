package nl.rug.search.cpptool.api;

import com.google.common.collect.ImmutableSet;
import nl.rug.search.cpptool.api.data.*;

import javax.annotation.Nonnull;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

public enum DeclType {
    VARIABLE(HasContext.FALSE, Variable.class, Named.class),
    FUNCTION(HasContext.TRUE, Function.class, ParamSet.class, Named.class, DeclContainer.class),
    TYPEDEF(HasContext.FALSE, Named.class, Typedef.class),
    RECORD(HasContext.TRUE, Record.class, Named.class, DeclContainer.class),
    ENUM(HasContext.TRUE, Named.class, DeclContainer.class),
    ISOLATED_CONTEXT(HasContext.TRUE, ParamSet.class, DeclContainer.class);

    public final HasContext hasContext;
    private final Set<Class<?>> dataTypes;

    DeclType(final HasContext context, final @Nonnull Class<?>... dataTypes) {
        this.hasContext = context;
        this.dataTypes = ImmutableSet.copyOf(dataTypes);
    }

    public boolean check(final @Nonnull Declaration decl) {
        checkNotNull(decl, "decl == NULL");
        return dataTypes.stream().allMatch(decl::has);
    }

    public enum HasContext {
        TRUE,
        FALSE
    }
}
