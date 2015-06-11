package nl.rug.search.cpptool.api;

import com.google.common.collect.ImmutableSet;
import nl.rug.search.cpptool.api.data.*;

import javax.annotation.Nonnull;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

public enum DeclType {
    VARIABLE(Variable.class, Named.class),
    FUNCTION(Function.class, ParamSet.class, Named.class),
    TYPEDEF(Named.class),
    RECORD(Record.class, Named.class),
    ENUM(Named.class),
    ISOLATED_CONTEXT(ParamSet.class);

    private final Set<Class<?>> dataTypes;

    DeclType(final @Nonnull Class<?>... dataTypes) {
        this.dataTypes = ImmutableSet.copyOf(dataTypes);
    }

    public boolean check(final @Nonnull Declaration decl) {
        checkNotNull(decl, "decl == NULL");
        return dataTypes.stream().allMatch(decl::has);
    }
}
