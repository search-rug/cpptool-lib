package nl.rug.search.cpptool.api.data;

import nl.rug.search.cpptool.api.Type;
import nl.rug.search.cpptool.runtime.ExtendedData;

import javax.annotation.Nonnull;

@ExtendedData(Variable.class)
public interface CxxVariable extends Variable {
    @Nonnull
    Type parentClass();
}
