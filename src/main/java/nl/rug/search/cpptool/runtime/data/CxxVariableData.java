package nl.rug.search.cpptool.runtime.data;

import com.google.common.base.MoreObjects;
import nl.rug.search.cpptool.api.Declaration;
import nl.rug.search.cpptool.api.Type;
import nl.rug.search.cpptool.api.data.CxxVariable;
import nl.rug.search.cpptool.api.data.Variable;

import javax.annotation.Nonnull;

public class CxxVariableData implements CxxVariable {
    private final Variable base;
    private final Type parent;

    private CxxVariableData(Variable base, Type parent) {
        this.base = base;
        this.parent = parent;
    }

    public static CxxVariableData build(Variable variable, Type type) {
        return new CxxVariableData(variable, type);
    }

    @Nonnull
    @Override
    public Type parentClass() {
        return this.parent;
    }

    @Nonnull
    @Override
    public Type type() {
        return this.base.type();
    }

    @Nonnull
    @Override
    public VariableKind kind() {
        return this.base.kind();
    }

    @Nonnull
    @Override
    public Declaration decl() {
        return this.base.decl();
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper("CxxVariable")
                .add("base", base)
                .add("parent", parentClass())
                .toString();
    }
}
