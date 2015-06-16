package nl.rug.search.cpptool.runtime.data;

import com.google.common.base.MoreObjects;
import nl.rug.search.cpptool.api.Declaration;
import nl.rug.search.cpptool.api.Type;
import nl.rug.search.cpptool.api.data.Typedef;
import nl.rug.search.cpptool.runtime.impl.DynamicLookup;
import nl.rug.search.cpptool.runtime.mutable.MType;

import javax.annotation.Nonnull;

public class TypedefData implements Typedef {
    private final Declaration decl;
    private final DynamicLookup<MType> type;

    public TypedefData(Declaration decl, DynamicLookup<MType> type) {
        this.decl = decl;
        this.type = type;
    }

    public static TypedefData build(Declaration decl, DynamicLookup<MType> type) {
        return new TypedefData(decl, type);
    }

    @Nonnull
    @Override
    public Type getMappedType() {
        return type.get();
    }

    @Nonnull
    @Override
    public Declaration decl() {
        return decl;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper("Typedef")
                .add("backing-type", type)
                .toString();
    }
}
