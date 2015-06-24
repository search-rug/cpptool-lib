package nl.rug.search.cpptool.api.data;

import com.google.common.collect.Iterables;
import nl.rug.search.cpptool.api.Type;
import nl.rug.search.cpptool.runtime.ExtendedData;

import javax.annotation.Nonnull;

/**
 * Data component that specializes a {@link Record} as a c++-style record.
 * This makes it possible for the record to have parent (also known as base) classes.
 *
 * @author David van Leusen <J.D.van.leusen@student.rug.nl>
 * @see <a href="http://en.cppreference.com/w/cpp/language/class">class declaration</a>
 * @since 2015-06-24
 */
@ExtendedData(Record.class)
public interface CxxRecord extends Record {
    @Nonnull
    Iterable<Type> parents();

    default boolean hasParents() {
        return !Iterables.isEmpty(this.parents());
    }
}
