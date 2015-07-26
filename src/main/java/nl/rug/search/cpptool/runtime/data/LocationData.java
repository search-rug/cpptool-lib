package nl.rug.search.cpptool.runtime.data;

import com.google.common.base.MoreObjects;
import nl.rug.search.cpptool.api.SourceFile;
import nl.rug.search.cpptool.api.data.Location;
import nl.rug.search.cpptool.runtime.impl.DynamicLookup;
import nl.rug.search.cpptool.proto.Base;

public class LocationData implements Location {
    private final DynamicLookup<? extends SourceFile> file;
    private final CCursor start;
    private final CCursor end;

    private LocationData(DynamicLookup<? extends SourceFile> file, CCursor start, CCursor end) {
        this.file = file;
        this.start = start;
        this.end = end;
    }

    public static LocationData build(DynamicLookup<? extends SourceFile> file, Base.SourceRange range) {
        return new LocationData(
                file,
                new CCursor(range.getStart().getLine(), range.getStart().getColumn()),
                new CCursor(range.getEnd().getLine(), range.getEnd().getColumn())
        );
    }

    @Override
    public SourceFile file() {
        return file.get();
    }

    @Override
    public Cursor start() {
        return start;
    }

    @Override
    public Cursor end() {
        return end;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper("Location")
                .add("file", file())
                .add("start", start)
                .add("end", end)
                .toString();
    }

    private static class CCursor implements Location.Cursor {
        private final int line;
        private final int column;

        public CCursor(int line, int column) {
            this.line = line;
            this.column = column;
        }

        @Override
        public int line() {
            return line;
        }

        @Override
        public int column() {
            return column;
        }

        @Override
        public String toString() {
            return line + ":" + column;
        }
    }
}
