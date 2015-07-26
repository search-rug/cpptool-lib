package nl.rug.search.cpptool.api.data;

/**
 * Enumaration providing possible types of access describes the possible access to {@link CxxFunction}, {@link Field},
 * and {@link CxxRecordParent}.
 *
 * @author Daniel Feitosa <d.feitosa@rug.nl>
 * @see <a href="http://en.cppreference.com/w/cpp/language/access">access specifier</a>
 * @since 2015-07-25
 */
public enum Access {
    PUBLIC,
    PROTECTED,
    PRIVATE
}
