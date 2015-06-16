package nl.rug.search.cpptool.runtime.data;

import com.google.common.base.MoreObjects;
import nl.rug.search.cpptool.api.data.Named;

import javax.annotation.Nonnull;

public class NamedData implements Named {
    private final String name;

    private NamedData(String name) {
        this.name = name;
    }

    public static NamedData build(String name) {
        return new NamedData(name);
    }

    @Nonnull
    @Override
    public String name() {
        return this.name;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper("Named")
                .add("name", name)
                .toString();
    }
}
