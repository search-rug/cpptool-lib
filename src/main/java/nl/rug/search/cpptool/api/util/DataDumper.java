package nl.rug.search.cpptool.api.util;

import nl.rug.search.proto.Wrapper;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;

/**
 * Utility application to dump .pb data files to the standard output using Protobuf's text representation.
 *
 * @author David van Leusen <J.D.van.leusen@student.rug.nl>
 * @since 2015-06-24
 */
public class DataDumper {
    public static void main(String args[]) {
        checkArgument(args.length > 0, "Please provide the .pb filepath to dump.");
        final File f = new File(args[0]);
        checkState(f.exists(), "The given file does not exist.");
        try (final FileInputStream fstream = new FileInputStream(f);
             final BufferedInputStream bstream = new BufferedInputStream(fstream)) {
            System.out.println(Wrapper.Prelude.parseDelimitedFrom(bstream));

            Wrapper.Envelope env;
            while ((env = Wrapper.Envelope.parseDelimitedFrom(bstream)) != null) {
                System.out.println(env);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
