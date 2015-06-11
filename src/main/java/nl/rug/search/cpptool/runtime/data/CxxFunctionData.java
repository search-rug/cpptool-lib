package nl.rug.search.cpptool.runtime.data;

import nl.rug.search.cpptool.api.Declaration;
import nl.rug.search.cpptool.api.Type;
import nl.rug.search.cpptool.api.data.CxxFunction;
import nl.rug.search.cpptool.api.data.Function;
import nl.rug.search.cpptool.api.data.Location;

import javax.annotation.Nonnull;
import java.util.Optional;

public class CxxFunctionData implements CxxFunction {
    private final Function base;

    public CxxFunctionData(Function base) {
        this.base = base;
    }

    @Nonnull
    @Override
    public Type parentClass() {
        return null;
    }

    @Override
    public boolean isVirtual() {
        return false;
    }

    @Override
    public boolean isStatic() {
        return false;
    }

    @Nonnull
    @Override
    public Type returnType() {
        return this.base.returnType();
    }

    @Nonnull
    @Override
    public Optional<Location> body() {
        return this.base.body();
    }

    @Nonnull
    @Override
    public Declaration decl() {
        return this.base.decl();
    }
}
