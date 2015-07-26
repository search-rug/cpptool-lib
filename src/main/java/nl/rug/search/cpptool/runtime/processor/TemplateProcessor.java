package nl.rug.search.cpptool.runtime.processor;

import com.google.common.collect.Lists;
import nl.rug.search.cpptool.api.data.Template;
import nl.rug.search.cpptool.runtime.data.TemplateData;
import nl.rug.search.cpptool.runtime.impl.DynamicLookup;
import nl.rug.search.cpptool.runtime.mutable.MDeclaration;
import nl.rug.search.cpptool.proto.Tmpl;

import java.util.Optional;

interface TemplateProcessor {
    ProtobufProcessor<Tmpl.Template> TEMPLATE = (context, message) -> {
        context.defer(() -> {
            DynamicLookup<MDeclaration> decl;
            switch (message.getAttachedToCase()) {
                case ATTACHED_TYPE:
                    decl = context.findDeclaration(message.getAttachedType());
                    break;
                case ATTACHED_NAME:
                    decl = context.findDeclaration(message.getAttachedName());
                    break;
                case ATTACHEDTO_NOT_SET:
                default:
                    throw new AssertionError("Malformed message, variation not set!");
            }

            if (!decl.toOptional().isPresent()) return;

            final MDeclaration actualDecl = decl.get();
            actualDecl.insertData(Template.class, TemplateData.build(
                    Lists.transform(message.getSpecializationsList(), context::findType)
            ));
        });

        return Optional.empty();
    };

    ProtobufProcessor<Tmpl.TemplateParam> TEMPLATE_PARAM = (context, message) -> {
        context.defer(() -> {
            DynamicLookup<MDeclaration> decl;
            switch (message.getOwnerCase()) {
                case OWNER_NAME:
                    decl = context.findDeclaration(message.getOwnerName());
                    break;
                case OWNER_TYPE:
                    decl = context.findDeclaration(message.getOwnerType());
                    break;
                case OWNER_NOT_SET:
                default:
                    throw new AssertionError("Malformed message, variation not set!");
            }

            if (!decl.toOptional().isPresent()) return;

            final MDeclaration actualDecl = decl.get();
            TemplateData.get(actualDecl).addParam(context.findType(message.getOwnType()));
        }, BuilderContext.DEFAULT_DEFER_PRIORITY + 100);
        //Deferred are executed according to natural ordering of priority, so this makes template arguments
        //resolve after the actual templates

        return Optional.empty();
    };
}
