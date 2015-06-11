package nl.rug.search.cpptool.runtime.data;

import nl.rug.search.cpptool.api.Declaration;
import nl.rug.search.cpptool.api.Type;
import nl.rug.search.cpptool.api.data.Function;
import nl.rug.search.cpptool.api.data.Location;

import javax.annotation.Nonnull;
import java.util.Optional;

public class FunctionData implements Function {
    @Nonnull
    @Override
    public Declaration decl() {
        return null;
    }

    @Nonnull
    @Override
    public Type returnType() {
        return null;
    }

    @Nonnull
    @Override
    public Optional<Location> body() {
        return null;
    }
}
