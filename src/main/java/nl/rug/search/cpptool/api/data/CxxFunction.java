package nl.rug.search.cpptool.api.data;

import nl.rug.search.cpptool.api.Type;
import nl.rug.search.cpptool.runtime.ExtendedData;

import javax.annotation.Nonnull;

/**
 * Data component that specializes a function declaration as a class member.
 * Any function that is part of a record will have this data component.
 * The record it belongs to can be accessed through {@link #parentClass()}.
 *
 * @author David van Leusen <J.D.van.leusen@student.rug.nl>
 * @see <a href="http://en.cppreference.com/w/cpp/language/member_functions">member functions</a>
 * @since 2015-06-24
 */
@ExtendedData(Function.class)
public interface CxxFunction extends Function {
    @Nonnull
    Type parentClass();

    Access access();

    boolean isVirtual();

    boolean isStatic();
}
