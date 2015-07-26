package nl.rug.search.cpptool.runtime.data;

import com.google.common.base.MoreObjects;
import nl.rug.search.cpptool.api.Type;
import nl.rug.search.cpptool.api.data.Access;
import nl.rug.search.cpptool.api.data.CxxRecord;
import nl.rug.search.cpptool.api.data.CxxRecordParent;
import nl.rug.search.cpptool.proto.Base;
import nl.rug.search.cpptool.runtime.impl.AccessMapper;

import javax.annotation.Nonnull;

public class CxxRecordParentData implements CxxRecordParent{
    private final Type type;
    private final Access access;

    private CxxRecordParentData(Type type, Access access) {
        this.type = type;
        this.access = access;
    }

    public static CxxRecordParentData build(Type type, Base.Access access) {
        return new CxxRecordParentData(type, AccessMapper.mapAccess(access));
    }

    @Nonnull
    @Override
    public Type type() { return this.type; }

    @Nonnull
    @Override
    public Access access() {
        return this.access;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper("CxxRecordParent")
                .add("type", type())
                .add("access", access())
                .toString();
    }
}
