package nl.rug.search.cpptool.runtime.processor;

import nl.rug.search.cpptool.api.DeclType;
import nl.rug.search.cpptool.api.data.CxxFunction;
import nl.rug.search.cpptool.api.data.Function;
import nl.rug.search.cpptool.api.data.ParamSet;
import nl.rug.search.cpptool.runtime.data.CxxFunctionData;
import nl.rug.search.cpptool.runtime.data.FunctionData;
import nl.rug.search.cpptool.runtime.data.ParamSetData;
import nl.rug.search.cpptool.runtime.mutable.MDeclaration;
import nl.rug.search.proto.Funcs;

import java.util.Optional;

interface FuncsProcessor {
    ProtobufProcessor<Funcs.FunctionDef> FUNCTION = (context, message) -> {
        final MDeclaration decl = context.createDeclaration(message.getName(), DeclType.FUNCTION);

        decl.insertData(Function.class, FunctionData.build(
                decl,
                context.findType(message.getReturnType()),
                context.toLocation(message.getBody(), message.hasBody())
        ));
        decl.insertData(ParamSet.class, ParamSetData.build());

        return Optional.of(decl);
    };

    ProtobufProcessor<Funcs.ClassFunctionDef> CXX_FUNCTION = (context, message) -> {
        MDeclaration impl = FUNCTION.process(context, message.getBase()).get();

        impl.insertData(CxxFunction.class, CxxFunctionData.build(
                impl.dataUnchecked(Function.class),
                context.findType(message.getParent()),
                message.getStatic(),
                message.getVirtual()
        ));

        return Optional.of(impl);
    };
}
