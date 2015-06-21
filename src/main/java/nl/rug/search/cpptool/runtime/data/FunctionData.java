package nl.rug.search.cpptool.runtime.data;

import com.google.common.base.MoreObjects;
import nl.rug.search.cpptool.api.Declaration;
import nl.rug.search.cpptool.api.Type;
import nl.rug.search.cpptool.api.data.Function;
import nl.rug.search.cpptool.api.data.Location;

import javax.annotation.Nonnull;
import java.util.Optional;

public class FunctionData implements Function {
    private final Declaration decl;
    private final Type returnType;
    private final Optional<Location> location;

    private FunctionData(Declaration decl, Type returnType, Optional<Location> location) {
        this.decl = decl;
        this.returnType = returnType;
        this.location = location;
    }

    public static FunctionData build(Declaration decl, Type type, Optional<Location> location) {
        return new FunctionData(decl, type, location);
    }

    @Nonnull
    @Override
    public Declaration decl() {
        return this.decl;
    }

    @Nonnull
    @Override
    public Type returnType() {
        return this.returnType;
    }

    @Nonnull
    @Override
    public Optional<Location> body() {
        return this.location;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper("Function")
                .add("return", returnType())
                .add("body", this.location)
                .toString();
    }
}
