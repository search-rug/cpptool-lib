package nl.rug.search.cpptool.runtime.data;

import com.google.common.base.MoreObjects;
import nl.rug.search.cpptool.api.Declaration;
import nl.rug.search.cpptool.api.Type;
import nl.rug.search.cpptool.api.data.CxxVariable;
import nl.rug.search.cpptool.api.data.Variable;
import nl.rug.search.cpptool.runtime.impl.DynamicLookup;
import nl.rug.search.cpptool.runtime.mutable.MType;

import javax.annotation.Nonnull;

public class CxxVariableData implements CxxVariable {
    private final Variable base;
    private final DynamicLookup<MType> parent;

    private CxxVariableData(Variable base, DynamicLookup<MType> parent) {
        this.base = base;
        this.parent = parent;
    }

    public static CxxVariableData build(Variable variable, DynamicLookup<MType> type) {
        return new CxxVariableData(variable, type);
    }

    @Nonnull
    @Override
    public Type parentClass() {
        return parent.get();
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
