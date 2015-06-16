package nl.rug.search.cpptool.runtime.processor;

import com.google.protobuf.MessageLite;
import nl.rug.search.proto.Wrapper;
import nl.rug.search.proto.Wrapper.Envelope.ContentCase;

import java.util.EnumMap;
import java.util.function.BiConsumer;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkState;

public class Processors {
    private final static EnumMap<ContentCase, BiConsumer<BuilderContext, Wrapper.Envelope>> PROCESSOR_MAP =
            new EnumMap<>(ContentCase.class);

    static {
        PROCESSOR_MAP.put(ContentCase.CONTENT_NOT_SET, (partialResult, envelope) -> {
            throw new AssertionError("Invalid message, content not set!");
        });

        //classes.proto
        addHandler(ContentCase.RECORD, ClassesProcessor.RECORD, Wrapper.Envelope::getRecord);
        addHandler(ContentCase.C_RECORD, ClassesProcessor.CXX_RECORD, Wrapper.Envelope::getCRecord);
        addHandler(ContentCase.ENUM_DEF, ClassesProcessor.ENUM, Wrapper.Envelope::getEnumDef);

        //funcs.proto
        addHandler(ContentCase.FUNC, FuncsProcessor.FUNCTION, Wrapper.Envelope::getFunc);
        addHandler(ContentCase.C_FUNC, FuncsProcessor.CXX_FUNCTION, Wrapper.Envelope::getCFunc);

        //vars.proto
        addHandler(ContentCase.VAR, VarsProcessor.VAR, Wrapper.Envelope::getVar);
        addHandler(ContentCase.C_VAR, VarsProcessor.CXX_VAR, Wrapper.Envelope::getCVar);

        //tmpl.proto
        addHandler(ContentCase.TMPL, TemplateProcessor.TEMPLATE, Wrapper.Envelope::getTmpl);
        addHandler(ContentCase.TMPL_PARAM, TemplateProcessor.TEMPLATE_PARAM, Wrapper.Envelope::getTmplParam);

        //base.proto
        addHandler(ContentCase.TYPE, BaseProcessor.TYPE_DEFINITION, Wrapper.Envelope::getType);
        addHandler(ContentCase.ISO_CONTEXT, BaseProcessor.ISO_CONTEXT, Wrapper.Envelope::getIsoContext);

        //misc.proto
        addHandler(ContentCase.INCLUDE, MiscProcessor.INCLUDE, Wrapper.Envelope::getInclude);
        addHandler(ContentCase.TDEF, MiscProcessor.TYPE_DEF, Wrapper.Envelope::getTdef);
        addHandler(ContentCase.FRIEND, MiscProcessor.FRIEND, Wrapper.Envelope::getFriend);
        addHandler(ContentCase.INPUT, MiscProcessor.INPUT_SWITCH, Wrapper.Envelope::getInput);
    }

    private static <M extends MessageLite> void addHandler(ContentCase type,
                                                           ProtobufProcessor<M> processor,
                                                           Function<Wrapper.Envelope, M> converter) {
        checkState(!PROCESSOR_MAP.containsKey(type));
        PROCESSOR_MAP.put(type, (o, envelope) -> processor.process(o, converter.apply(envelope)));
    }

    public static void process(BuilderContext context, Wrapper.Envelope env) {
        PROCESSOR_MAP.get(env.getContentCase()).accept(context, env);
    }
}
