package nl.rug.search.cpptool.api.data;

import com.google.common.collect.Iterables;
import nl.rug.search.cpptool.api.Type;
import nl.rug.search.cpptool.runtime.ExtendedData;

import javax.annotation.Nonnull;

@ExtendedData(Record.class)
public interface CxxRecord extends Record {
    @Nonnull
    Iterable<Type> parents();

    default boolean hasParents() {
        return !Iterables.isEmpty(this.parents());
    }
}
