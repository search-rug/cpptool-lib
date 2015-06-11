package nl.rug.search.cpptool.api.data;

import nl.rug.search.cpptool.api.Type;
import nl.rug.search.cpptool.runtime.ExtendedData;

import javax.annotation.Nonnull;

@ExtendedData(Function.class)
public interface CxxFunction extends Function {
    @Nonnull
    Type parentClass();

    boolean isVirtual();

    boolean isStatic();
}
