package nl.rug.search.cpptool.api;

import com.google.common.collect.ImmutableSet;
import nl.rug.search.cpptool.api.data.Function;
import nl.rug.search.cpptool.api.data.ParamSet;
import nl.rug.search.cpptool.api.data.Record;
import nl.rug.search.cpptool.api.data.Variable;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.Set;

import static com.google.common.base.Preconditions.checkNotNull;

public enum DeclType {
    VARIABLE(Variable.class),
    FUNCTION(Function.class, ParamSet.class),
    TYPEDEF(),
    RECORD(Record.class),
    ENUM(),
    ISOLATED_CONTEXT(ParamSet.class);

    private final Set<Class<?>> dataTypes;

    DeclType(final @Nonnull Class<?>... dataTypes) {
        this.dataTypes = ImmutableSet.copyOf(dataTypes);
    }

    public boolean check(final @Nonnull Declaration decl) {
        checkNotNull(decl, "decl == NULL");
        return dataTypes.stream().map(decl::data).allMatch(Optional::isPresent);
    }
}
