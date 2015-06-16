package nl.rug.search.cpptool.runtime.data;

import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import nl.rug.search.cpptool.api.Declaration;
import nl.rug.search.cpptool.api.Type;
import nl.rug.search.cpptool.api.data.CxxRecord;
import nl.rug.search.cpptool.api.data.Record;
import nl.rug.search.cpptool.runtime.impl.DynamicLookup;
import nl.rug.search.cpptool.runtime.mutable.MType;

import javax.annotation.Nonnull;
import java.util.List;

public class CxxRecordData implements CxxRecord {
    private final Record base;
    private final ImmutableList<DynamicLookup<MType>> parents;

    public CxxRecordData(Record base, ImmutableList<DynamicLookup<MType>> parents) {
        this.base = base;
        this.parents = parents;
    }

    public static CxxRecordData build(Record base, List<DynamicLookup<MType>> parents) {
        return new CxxRecordData(base, ImmutableList.copyOf(parents));
    }

    @Nonnull
    @Override
    public Iterable<Type> parents() {
        return Lists.transform(parents, DynamicLookup::get);
    }

    @Nonnull
    @Override
    public Type type() {
        return this.base.type();
    }

    @Nonnull
    @Override
    public Variant variant() {
        return this.base.variant();
    }

    @Nonnull
    @Override
    public Declaration decl() {
        return this.base.decl();
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper("CxxRecord")
                .add("base", base)
                .add("parents", Iterables.toString(parents()))
                .toString();
    }
}
