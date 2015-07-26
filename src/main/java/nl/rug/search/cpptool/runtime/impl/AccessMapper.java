package nl.rug.search.cpptool.runtime.impl;

import nl.rug.search.cpptool.api.data.Access;
import nl.rug.search.cpptool.proto.Base;

import javax.annotation.Nonnull;

public class AccessMapper {

    @Nonnull
    public static Access mapAccess(Base.Access access) {
        switch (access) {
            case PUBLIC:
                return Access.PUBLIC;
            case PROTECTED:
                return Access.PROTECTED;
            case PRIVATE:
                return Access.PRIVATE;
            case UNKNOWN:
            default:
                throw new AssertionError("Unknown access specifier: " + access);
        }
    }
}
