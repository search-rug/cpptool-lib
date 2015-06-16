package nl.rug.search.cpptool.runtime.impl;

import com.google.common.base.MoreObjects;
import nl.rug.search.cpptool.api.Declaration;
import nl.rug.search.cpptool.api.data.Location;
import nl.rug.search.cpptool.runtime.mutable.MDeclaration;
import nl.rug.search.cpptool.runtime.mutable.MType;
import nl.rug.search.proto.Base;

import javax.annotation.Nonnull;
import java.util.Optional;

import static nl.rug.search.cpptool.runtime.util.Coerce.coerce;

class InternalType implements MType {
    private final String name;
    private final Optional<Location> location;
    private final boolean isStronglyDefined;
    private RelocatableProperty<MDeclaration> decl;

    public InternalType(Base.ScopedName name, RelocatableProperty<MDeclaration> decl, Optional<Location> location, boolean isStronglyDefined) {
        this.name = simplify(name);
        this.decl = decl;
        this.location = location;
        this.isStronglyDefined = isStronglyDefined;
    }

    private static String simplify(Base.ScopedName name) {
        return Optional.ofNullable(name.getContext()).orElse("") + name.getName();
    }

    @Nonnull
    @Override
    public String name() {
        return this.name;
    }

    @Nonnull
    @Override
    public Optional<Declaration> declaration() {
        return coerce(this.decl.toOptional());
    }

    @Nonnull
    @Override
    public Optional<Location> location() {
        return this.location;
    }

    @Override
    public boolean isStronglyDefined() {
        return this.isStronglyDefined;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("name", name)
                .add("stronglyDefined", isStronglyDefined)
                .toString();
    }

    @Override
    public void updateDeclaration(final @Nonnull MDeclaration decl) {
        this.decl.set(decl);
    }

    @Override
    public DynamicLookup<MDeclaration> getDeclaration() {
        return this.decl;
    }
}
