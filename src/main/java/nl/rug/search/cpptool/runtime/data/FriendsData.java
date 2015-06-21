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
    private final List<DynamicLookup<Declaration>> friends = Lists.newLinkedList();
    private final List<DynamicLookup<Declaration>> friendedBy = Lists.newLinkedList();

    public static void addFriendship(DynamicLookup<? extends MDeclaration> friender,
                                     DynamicLookup<? extends MDeclaration> friended) {
        //TODO: friends might not exist as declarations, perhaps just store signatures and
        //TODO: add options to do lookup?
        //getOrInit(friender.get()).friends.add(coerce(friended));
        //getOrInit(friended.get()).friendedBy.add(coerce(friender));
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
    @Override
    public Iterable<Declaration> friends() {
        return Lists.transform(Collections.unmodifiableList(friends), DynamicLookup::get);
    }

    @Nonnull
    @Override
    public Iterable<Declaration> friendOf() {
        return Lists.transform(Collections.unmodifiableList(friendedBy), DynamicLookup::get);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper("Friends")
                .add("friends", Iterables.toString(Iterables.transform(friends(), Declaration::name)))
                .add("friendedBy", Iterables.toString(Iterables.transform(friendOf(), Declaration::name)))
                .toString();
    }
}
