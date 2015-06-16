package nl.rug.search.cpptool.runtime.processor;

import nl.rug.search.cpptool.api.data.ParamSet;
import nl.rug.search.cpptool.runtime.data.ParamSetData;
import nl.rug.search.cpptool.runtime.mutable.MDeclaration;
import nl.rug.search.proto.Base;

import java.util.Optional;

interface BaseProcessor {
    ProtobufProcessor<Base.TypeDefinition> TYPE_DEFINITION = (context, message) -> {
        context.createTypeMapping(message);
        return Optional.empty();
    };

    ProtobufProcessor<Base.IsolatedContextDefinition> ISO_CONTEXT = (context, message) -> {
        final MDeclaration decl = context.createIsolatedContext(message);
        decl.insertData(ParamSet.class, ParamSetData.build());
        context.updateTypeMapping(message.getOwnType(), decl);
        return Optional.of(decl);
    };
}
