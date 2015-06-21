package nl.rug.search.cpptool.runtime.data;

import com.google.common.base.MoreObjects;
import nl.rug.search.cpptool.api.Declaration;
import nl.rug.search.cpptool.api.Type;
import nl.rug.search.cpptool.api.data.Typedef;

import javax.annotation.Nonnull;

public class TypedefData implements Typedef {
    private final Declaration decl;
    private final Type type;

    public TypedefData(Declaration decl, Type type) {
        this.decl = decl;
        this.type = type;
    }

    public static TypedefData build(Declaration decl, Type type) {
        return new TypedefData(decl, type);
    }

    @Nonnull
    @Override
    public Type getMappedType() {
        return this.type;
    }

    @Nonnull
    @Override
    public Declaration decl() {
        return this.decl;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper("Typedef")
                .add("backing-type", type)
                .toString();
    }
}
