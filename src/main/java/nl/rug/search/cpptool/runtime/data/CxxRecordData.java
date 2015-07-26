package nl.rug.search.cpptool.runtime.data;

import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import nl.rug.search.cpptool.api.Declaration;
import nl.rug.search.cpptool.api.Type;
import nl.rug.search.cpptool.api.data.CxxRecord;
import nl.rug.search.cpptool.api.data.CxxRecordParent;
import nl.rug.search.cpptool.api.data.Record;

import javax.annotation.Nonnull;
import java.util.List;

public class CxxRecordData implements CxxRecord {
    private final Record base;
    private final ImmutableList<CxxRecordParent> parents;

    public CxxRecordData(Record base, ImmutableList<CxxRecordParent> parents) {
        this.base = base;
        this.parents = parents;
    }

    public static CxxRecordData build(Record base, List<CxxRecordParent> parents) {
        return new CxxRecordData(base, ImmutableList.copyOf(parents));
    }

    @Nonnull
    @Override
    public Iterable<CxxRecordParent> parents() {
        return this.parents;
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
