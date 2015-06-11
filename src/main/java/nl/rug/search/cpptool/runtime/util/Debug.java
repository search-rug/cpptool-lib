package nl.rug.search.cpptool.runtime.util;

import javax.annotation.Nonnull;

public class Debug {
    @Nonnull
    @Deprecated
    public static RuntimeException NYI() {
        return new UnsupportedOperationException("Not Yet Implemented");
    }
}
