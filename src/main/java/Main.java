import nl.rug.search.cpptool.api.DeclContainer;
import nl.rug.search.cpptool.api.io.Assembler;

import java.io.File;

import static com.google.common.base.Preconditions.checkNotNull;

public class Main {
    public static void main(String args[]) throws InterruptedException {
        final Assembler assembler = Assembler.create();

        for (File f : checkNotNull(new File(args[0]).listFiles())) {
            assembler.read(f);
        }

        DeclContainer result = assembler.build();

        result.includes().forEach(System.out::println);
    }
}
