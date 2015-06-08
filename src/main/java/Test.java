import com.google.protobuf.CodedInputStream;
import com.google.protobuf.Parser;
import nl.rug.search.proto.Wrapper;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class Test {
    public static void main(String args[]) {
        for (File f : new File(args[0]).listFiles()) {
            System.out.printf("Reading: %s%n", f.getPath());
            try (FileInputStream fstream = new FileInputStream(f)) {
                final CodedInputStream cstream = CodedInputStream.newInstance(fstream);
                readout(cstream);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void readout(final CodedInputStream input) throws IOException {
        System.out.printf(
                "Original file: %s%n",
                readDelimited(input, Wrapper.Prelude.PARSER).getTargetFile()
        );

        while (!input.isAtEnd()) {
            System.out.printf("Total read: %d%n", input.getTotalBytesRead());
            printContents(readDelimited(input, Wrapper.Envelope.PARSER));
        }
    }

    public static void printContents(final Wrapper.Envelope env) {
        System.out.printf("ITEM: %s%n", env.getContentCase().name());
    }

    private static <T> T readDelimited(final CodedInputStream input, final Parser<T> parser) throws IOException {
        int len = input.readRawVarint32();
        System.out.printf("Size: %d%n", len);
        int oldLimit = input.pushLimit(len);
        T msg = parser.parseFrom(input);
        input.popLimit(oldLimit);
        return msg;
    }
}
