package nl.rug.search.cpptool.runtime.data;

import com.google.common.base.MoreObjects;
import nl.rug.search.cpptool.api.Declaration;
import nl.rug.search.cpptool.api.Type;
import nl.rug.search.cpptool.api.data.Access;
import nl.rug.search.cpptool.api.data.Field;
import nl.rug.search.cpptool.api.data.Variable;
import nl.rug.search.cpptool.proto.Base;
import nl.rug.search.cpptool.runtime.impl.AccessMapper;

import javax.annotation.Nonnull;

public class FieldData implements Field {
    private final Variable base;
    private final Type parent;
    private final Access access;

    private FieldData(Variable base, Type parent, Access access) {
        this.base = base;
        this.parent = parent;
        this.access = access;
    }

    public static FieldData build(Variable variable, Type type, Base.Access access) {
        return new FieldData(variable, type, AccessMapper.mapAccess(access));
    }

    @Nonnull
    @Override
    public Type parentClass() {
        return this.parent;
    }

    @Nonnull
    @Override
    public Access access() {
        return this.access;
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
        return MoreObjects.toStringHelper("Field")
                .add("base", base)
                .add("parent", parentClass())
                .add("access", access())
                .toString();
    }
}
