package nl.rug.search.cpptool.runtime.data;

import com.google.common.base.MoreObjects;
import nl.rug.search.cpptool.api.Declaration;
import nl.rug.search.cpptool.api.Type;
import nl.rug.search.cpptool.api.data.Variable;
import nl.rug.search.cpptool.runtime.impl.DynamicLookup;
import nl.rug.search.cpptool.runtime.mutable.MType;
import nl.rug.search.proto.Vars;

import javax.annotation.Nonnull;

public class VariableData implements Variable {
    private final Declaration decl;
    private final DynamicLookup<MType> type;
    private final VariableKind kind;

    public VariableData(Declaration decl, DynamicLookup<MType> type, VariableKind kind) {
        this.decl = decl;
        this.type = type;
        this.kind = kind;
    }

    @Nonnull
    public static VariableData build(Declaration decl, DynamicLookup<MType> type, Vars.Var.VarKind kind) {
        return new VariableData(decl, type, mapVarKind(kind));
    }

    @Nonnull
    private static VariableKind mapVarKind(Vars.Var.VarKind kind) {
        switch (kind) {
            case LOCAL:
                return VariableKind.LOCAL;
            case PARAMETER:
                return VariableKind.PARAMETER;
            case GLOBAL:
                return VariableKind.GLOBAL;
            case CLASS:
                return VariableKind.CLASS_MEMBER;
            case UNKNOWN:
            default:
                throw new AssertionError("Unknown variable type: " + kind);
        }
    }

    @Nonnull
    @Override
    public Type type() {
        return this.type.get();
    }

    @Nonnull
    @Override
    public VariableKind kind() {
        return this.kind;
    }

    @Nonnull
    @Override
    public Declaration decl() {
        return this.decl;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper("Variable")
                .add("type", type)
                .add("kind", kind)
                .toString();
    }
}
