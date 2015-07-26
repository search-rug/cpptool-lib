package nl.rug.search.cpptool.api.data;

import nl.rug.search.cpptool.api.Type;

/**
 * Data component that is attached to all {@link CxxRecord} declarations.
 *
 * @author Daniel Feitosa <d.feitosa@rug.nl>
 * @see <a href="http://en.cppreference.com/w/cpp/language/derived_class">derived class</a>
 * @since 2015-07-25
 */
public interface CxxRecordParent {
    Type type();

    Access access();
}
