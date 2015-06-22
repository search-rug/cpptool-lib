package nl.rug.search.cpptool.api.data;

import nl.rug.search.cpptool.api.Declaration;

import javax.annotation.Nonnull;
import java.util.Optional;

/**
 * This data class is not used at the moment. {@link nl.rug.search.cpptool.runtime.processor.MiscProcessor#FRIEND}
 */
@Deprecated
public interface Friends {
    @Nonnull
    Iterable<String> friends();

    @Nonnull
    Iterable<ResolvedFriend> resolvedFriends();

    interface ResolvedFriend {
        @Nonnull
        String signature();

        @Nonnull
        Optional<Declaration> resolve();
    }
}
