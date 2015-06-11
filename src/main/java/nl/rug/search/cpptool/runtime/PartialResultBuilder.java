package nl.rug.search.cpptool.runtime;

import java.io.File;
import java.util.concurrent.Callable;

import static com.google.common.base.Preconditions.checkState;

public class PartialResultBuilder implements Callable<PartialResult> {
    private final File pbFile;

    public PartialResultBuilder(final File pbFile) {
        this.pbFile = pbFile;
    }

    @Override
    public PartialResult call() throws Exception {
        checkState(pbFile.exists(), String.format("File '%s' does not exist.", pbFile.getPath()));
        System.out.printf("Reading: %s%n", pbFile.getPath());
        return null;
    }
}
