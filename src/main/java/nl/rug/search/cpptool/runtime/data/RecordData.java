package nl.rug.search.cpptool.runtime.data;

import com.google.common.base.MoreObjects;
import nl.rug.search.cpptool.api.Declaration;
import nl.rug.search.cpptool.api.Type;
import nl.rug.search.cpptool.api.data.Record;
import nl.rug.search.cpptool.runtime.impl.DynamicLookup;
import nl.rug.search.cpptool.runtime.mutable.MType;
import nl.rug.search.proto.Classes;

import javax.annotation.Nonnull;

public class RecordData implements Record {
    private final Declaration decl;
    private final DynamicLookup<MType> type;
    private final Variant variant;

    private RecordData(Declaration decl, DynamicLookup<MType> type, Variant variant) {
        this.decl = decl;
        this.type = type;
        this.variant = variant;
    }

    public static RecordData build(Declaration decl, DynamicLookup<MType> type, Classes.RecordDef.Variant variant) {
        return new RecordData(decl, type, transformVariant(variant));
    }

    private static Variant transformVariant(Classes.RecordDef.Variant variant) {
        switch (variant) {
            case CLASS:
                return Variant.CLASS;
            case STRUCT:
                return Variant.STRUCT;
            case UNION:
                return Variant.UNION;
            case UNKNOWN:
            default:
                throw new AssertionError("Unknown class type: " + variant);
        }
    }

    @Nonnull
    @Override
    public Type type() {
        return type.get();
    }

    @Nonnull
    @Override
    public Variant variant() {
        return this.variant;
    }

    @Nonnull
    @Override
    public Declaration decl() {
        return decl;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper("Record")
                .add("type", type)
                .add("variant", variant)
                .toString();
    }
}
