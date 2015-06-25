package nl.rug.search.cpptool.runtime.impl;


import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import nl.rug.search.cpptool.api.Declaration;
import nl.rug.search.cpptool.api.Type;
import nl.rug.search.cpptool.api.data.Location;
import nl.rug.search.cpptool.runtime.mutable.MDeclaration;
import nl.rug.search.cpptool.runtime.mutable.MType;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkNotNull;

class ModifiedType implements MType {
    private final InternalType type;
    private final ImmutableList<Modifier> modifiers;

    public ModifiedType(InternalType type, ImmutableList<Modifier> modifiers) {
        this.type = type;
        this.modifiers = modifiers;
    }

    @Nonnull
    @Override
    public DynamicLookup<MDeclaration> decl() {
        return this.type.decl();
    }

    @Nonnull
    @Override
    public MType withModifiers(@Nonnull Modifier... modifiers) {
        checkNotNull(modifiers, "modifiers == NULL");
        return this.type.withModifiers(Iterables.toArray(Iterables.concat(
                this.modifiers,
                Arrays.asList(modifiers)
        ), Modifier.class));
    }

    @Nonnull
    @Override
    public String name() {
        return this.type.name();
    }

    @Nonnull
    @Override
    public Optional<Declaration> declaration() {
        return this.type.declaration();
    }

    @Nonnull
    @Override
    public Optional<Location> location() {
        return this.type.location();
    }

    @Nonnull
    @Override
    public Iterable<Modifier> modifiers() {
        return this.modifiers;
    }

    @Nonnull
    @Override
    public Type unwrappedType() {
        return this.type;
    }

    @Override
    public boolean isStronglyDefined() {
        return this.type.isStronglyDefined();
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper("ModifiedType")
                .add("type", type)
                .add("modifiers", modifiers)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ModifiedType that = (ModifiedType) o;
        return Objects.equals(type, that.type) &&
                Objects.equals(modifiers, that.modifiers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, modifiers);
    }
}
