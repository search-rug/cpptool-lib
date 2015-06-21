package nl.rug.search.cpptool.runtime;

import nl.rug.search.cpptool.runtime.processor.Processors;
import nl.rug.search.proto.Wrapper;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.concurrent.Callable;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Preconditions.checkState;

class PartialResultBuilder implements Callable<PartialResult> {
    private final File pbFile;

    public PartialResultBuilder(final File pbFile) {
        this.pbFile = pbFile;
    }

    @Override
    public PartialResult call() throws Exception {
        checkState(pbFile.exists(), String.format("File '%s' does not exist.", pbFile.getPath()));

        try (final FileInputStream fstream = new FileInputStream(pbFile);
             final BufferedInputStream bstream = new BufferedInputStream(fstream)) {
            final PartialResult result =
                    new PartialResult(Wrapper.Prelude.parseDelimitedFrom(bstream).getTargetFile());

            Wrapper.Envelope env;
            while ((env = Wrapper.Envelope.parseDelimitedFrom(bstream)) != null) {
                Processors.process(result, checkNotNull(env));
            }

            result.performDeferredActions();

            return result;
        }
    }
}
