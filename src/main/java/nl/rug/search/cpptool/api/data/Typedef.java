package nl.rug.search.cpptool.api.data;

import nl.rug.search.cpptool.api.Attached;
import nl.rug.search.cpptool.api.Type;

import javax.annotation.Nonnull;

public interface Typedef extends Attached {
    @Nonnull
    Type getMappedType();
}
