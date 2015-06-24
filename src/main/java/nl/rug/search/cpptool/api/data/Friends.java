package nl.rug.search.cpptool.api.data;

import nl.rug.search.cpptool.api.Declaration;

import javax.annotation.Nonnull;
import java.util.Optional;

/**
 * Data component that specifies a list of declarations that can access the (private) data members of the associated
 * declaration, this declaration has to be a {@link CxxRecord} as well.
 *
 * @author David van Leusen <J.D.van.leusen@student.rug.nl>
 * @see <a href="http://en.cppreference.com/w/cpp/language/friend">friend declaration</a>
 * @since 2015-06-24
 */
public interface Friends {
    @Nonnull
    Iterable<String> friends();

    /**
     * Attempt to resolve the declarations that are considered friends of this declaration.
     * If a declaration is friended which is not a part of the input data, that declaration will not be returned.
     */
    @Nonnull
    Iterable<ResolvedFriend> resolvedFriends();

    interface ResolvedFriend {
        @Nonnull
        String signature();

        @Nonnull
        Optional<Declaration> resolve();
    }
}
