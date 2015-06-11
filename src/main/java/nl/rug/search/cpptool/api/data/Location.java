package nl.rug.search.cpptool.api.data;

import nl.rug.search.cpptool.api.SourceFile;

public interface Location {
    SourceFile file();

    Cursor start();

    Cursor end();

    interface Cursor {
        int line();

        int column();
    }
}
