package nl.rug.search.cpptool.runtime.data;

import com.google.common.base.MoreObjects;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import nl.rug.search.cpptool.api.Declaration;
import nl.rug.search.cpptool.api.data.Friends;
import nl.rug.search.cpptool.runtime.impl.DynamicLookup;
import nl.rug.search.cpptool.runtime.mutable.MDeclaration;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static nl.rug.search.cpptool.runtime.util.Coerce.coerce;

public class FriendsData implements Friends {
    private final List<ResolvedFriendImpl> friends = Lists.newLinkedList();

    public static void addFriendship(DynamicLookup<? extends MDeclaration> friender,
                                     String signature,
                                     DynamicLookup<? extends MDeclaration> friended) {
        getOrInit(friender.get()).friends.add(new ResolvedFriendImpl(signature, friended));
    }

    private static FriendsData getOrInit(MDeclaration decl) {
        final Optional<Friends> maybeFriends = decl.data(Friends.class);
        if (maybeFriends.isPresent()) {
            return (FriendsData) maybeFriends.get();
        } else {
            final FriendsData newFriendsData = new FriendsData();
            decl.insertData(Friends.class, newFriendsData);
            return newFriendsData;
        }
    }

    @Nonnull
    public Iterable<String> friends() {
        return Lists.transform(Collections.unmodifiableList(friends), ResolvedFriend::signature);
    }

    @Nonnull
    @Override
    public Iterable<ResolvedFriend> resolvedFriends() {
        return coerce(Collections.unmodifiableList(friends));
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper("Friends")
                .add("friends", Iterables.toString(friends()))
                .toString();
    }

    private static class ResolvedFriendImpl implements ResolvedFriend {
        private final String signature;
        private final DynamicLookup<Declaration> friend;

        public ResolvedFriendImpl(String signature, DynamicLookup<? extends MDeclaration> friend) {
            this.signature = signature;
            this.friend = coerce(friend);
        }

        @Nonnull
        @Override
        public String signature() {
            return this.signature;
        }

        @Nonnull
        @Override
        public Optional<Declaration> resolve() {
            return friend.toOptional();
        }
    }
}
