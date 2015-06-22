package nl.rug.search.cpptool.runtime.processor;

import nl.rug.search.cpptool.api.DeclType;
import nl.rug.search.cpptool.api.data.Typedef;
import nl.rug.search.cpptool.runtime.data.FriendsData;
import nl.rug.search.cpptool.runtime.data.TypedefData;
import nl.rug.search.cpptool.runtime.impl.DynamicLookup;
import nl.rug.search.cpptool.runtime.mutable.MDeclaration;
import nl.rug.search.cpptool.runtime.mutable.MSourceFile;
import nl.rug.search.proto.Misc;

import java.util.Optional;

interface MiscProcessor {
    ProtobufProcessor<Misc.IncludeRelation> INCLUDE = (context, message) -> {
        final MSourceFile origin = context.file(message.getOrigin()).get();
        final MSourceFile target = context.file(message.getTarget()).get();
        origin.includes(target);
        return Optional.empty();
    };

    ProtobufProcessor<Misc.TypeDef> TYPE_DEF = (context, message) -> {
        final MDeclaration decl = context.createDeclaration(message.getName(), DeclType.TYPEDEF);

        decl.insertData(Typedef.class, TypedefData.build(
                decl,
                context.findType(message.getMappedType())
        ));

        return Optional.of(decl);
    };

    ProtobufProcessor<Misc.FriendRelation> FRIEND = (context, message) -> {
        //TODO: unsupported for now, would require very late resolving to actually work
        if (false) {
            context.defer(() -> {
                DynamicLookup<MDeclaration> newFriend;
                switch (message.getVariationCase()) {
                    case FRIEND:
                        newFriend = context.findDeclaration(message.getFriend());
                        break;
                    case TYPE_FRIEND:
                        newFriend = context.findDeclaration(message.getTypeFriend());
                        break;
                    case VARIATION_NOT_SET:
                    default:
                        throw new AssertionError("Malformed message, variation not set!");
                }
                final DynamicLookup<MDeclaration> me = context.findDeclaration(message.getOrigin());

                final String sig = newFriend.toOptional().map(MDeclaration::signature).orElseGet(() -> {
                    //TODO: how to find signature
                    return "";
                });

                //Add as 'newFriend' as friend to 'me'
                FriendsData.addFriendship(me, sig, newFriend);
            });
        }

        return Optional.empty();
    };

    ProtobufProcessor<Misc.InputSwitch> INPUT_SWITCH = (context, message) -> {
        context.switchInputFile(message.getFilePath());
        return Optional.empty();
    };
}
