package nl.rug.search.cpptool.runtime.processor;

import com.google.common.collect.Lists;
import nl.rug.search.cpptool.api.DeclType;
import nl.rug.search.cpptool.api.data.CxxRecord;
import nl.rug.search.cpptool.api.data.Location;
import nl.rug.search.cpptool.api.data.Record;
import nl.rug.search.cpptool.runtime.data.CxxRecordData;
import nl.rug.search.cpptool.runtime.data.RecordData;
import nl.rug.search.cpptool.runtime.mutable.MDeclaration;
import nl.rug.search.proto.Classes;

import java.util.Optional;

interface ClassesProcessor {
    ProtobufProcessor<Classes.RecordDef> RECORD = (context, message) -> {
        MDeclaration decl = context.createDeclaration(message.getName(), DeclType.RECORD);

        decl.insertData(Record.class, RecordData.build(
                decl,
                context.findType(message.getInternalType()),
                message.getVariant()
        ));

        if (message.hasDefinition()) {
            decl.insertData(Location.class, context.toLocation(message.getDefinition()));
        }

        return Optional.of(decl);
    };

    ProtobufProcessor<Classes.CxxRecordDef> CXX_RECORD = (context, message) -> {
        MDeclaration impl = RECORD.process(context, message.getBase()).get();

        impl.insertData(CxxRecord.class, CxxRecordData.build(
                impl.dataUnchecked(Record.class),
                Lists.transform(message.getParentsList(), context::findType)
        ));

        return Optional.of(impl);
    };

    ProtobufProcessor<Classes.EnumDef> ENUM = (context, message) -> {
        final MDeclaration decl = context.createDeclaration(message.getName(), DeclType.ENUM);

        if (message.hasDefinition()) {
            decl.insertData(Location.class, context.toLocation(message.getDefinition()));
        }

        return Optional.of(decl);
    };
}
