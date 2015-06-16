package nl.rug.search.cpptool.runtime.processor;

import com.google.protobuf.MessageLite;
import nl.rug.search.cpptool.runtime.mutable.MDeclaration;

import java.util.Optional;
import java.util.function.BiFunction;

interface ProtobufProcessor<M extends MessageLite> extends BiFunction<BuilderContext, M, Optional<MDeclaration>> {
    Optional<MDeclaration> process(BuilderContext context, M message);

    @Override
    default Optional<MDeclaration> apply(BuilderContext context, M message) {
        return this.process(context, message);
    }
}
