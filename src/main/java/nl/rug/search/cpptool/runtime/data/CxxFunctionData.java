package nl.rug.search.cpptool.runtime.data;

import com.google.common.base.MoreObjects;
import nl.rug.search.cpptool.api.Declaration;
import nl.rug.search.cpptool.api.Type;
import nl.rug.search.cpptool.api.data.CxxFunction;
import nl.rug.search.cpptool.api.data.Function;
import nl.rug.search.cpptool.api.data.Location;

import javax.annotation.Nonnull;
import java.util.Optional;

public class CxxFunctionData implements CxxFunction {
    private final Function base;
    private final Type type;
    private final boolean isStatic;
    private final boolean isVirtual;

    private CxxFunctionData(Function base, Type type, boolean isStatic, boolean isVirtual) {
        this.base = base;
        this.type = type;
        this.isStatic = isStatic;
        this.isVirtual = isVirtual;
    }

    public static CxxFunctionData build(Function base, Type type, boolean isStatic, boolean isVirtual) {
        return new CxxFunctionData(base, type, isStatic, isVirtual);
    }

    @Nonnull
    @Override
    public Type parentClass() {
        return this.type;
    }

    @Override
    public boolean isVirtual() {
        return this.isVirtual;
    }

    @Override
    public boolean isStatic() {
        return this.isStatic;
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

    @Override
    public String toString() {
        return MoreObjects.toStringHelper("CxxFunction")
                .add("base", base)
                .add("class", parentClass())
                .toString();
    }
}
