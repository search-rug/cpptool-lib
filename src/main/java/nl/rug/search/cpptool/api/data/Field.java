package nl.rug.search.cpptool.api.data;

import nl.rug.search.cpptool.api.Type;
import nl.rug.search.cpptool.runtime.ExtendedData;

import javax.annotation.Nonnull;

/**
 * Data component that specializes a variable declaration as a class field.
 * Any field that is part of a record will have this data component.
 * The record it belongs to can be accessed through {@link #parentClass()}.
 *
 * @author David van Leusen <J.D.van.leusen@student.rug.nl>
 * @see <a href="http://en.cppreference.com/w/cpp/language/data_members">data members</a>
 * @since 2015-06-24
 */
@ExtendedData(Variable.class)
public interface Field extends Variable {
    @Nonnull
    Type parentClass();

    Access access();
}
