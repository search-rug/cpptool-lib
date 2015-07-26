package nl.rug.search.cpptool.runtime.impl;

import com.google.common.base.MoreObjects;
import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import nl.rug.search.cpptool.api.Declaration;
import nl.rug.search.cpptool.api.Type;
import nl.rug.search.cpptool.api.data.Location;
import nl.rug.search.cpptool.runtime.mutable.MDeclaration;
import nl.rug.search.cpptool.runtime.mutable.MType;
import nl.rug.search.cpptool.runtime.util.ContextPath;
import nl.rug.search.cpptool.proto.Base;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static nl.rug.search.cpptool.runtime.util.Coerce.coerce;

class InternalType implements MType {
    private final String name;
    private final Optional<Location> location;
    private final boolean isStronglyDefined;
    private final DynamicLookup<MDeclaration> decl;

    public InternalType(Base.ScopedName name, DynamicLookup<MDeclaration> decl, Optional<Location> location, boolean isStronglyDefined) {
        this.name = ContextPath.simplify(name);
        this.decl = decl;
        this.location = location;
        this.isStronglyDefined = isStronglyDefined;
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

    @Nonnull
    @Override
    public Iterable<Modifier> modifiers() {
        return ImmutableList.of();
    }

    @Nonnull
    @Override
    public Type unwrappedType() {
        return this;
    }

    @Override
    public boolean isStronglyDefined() {
        return this.isStronglyDefined;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper("Type")
                .add("name", name)
                .add("stronglyDefined", isStronglyDefined)
                .toString();
    }

    @Nonnull
    @Override
    public DynamicLookup<MDeclaration> decl() {
        return this.decl;
    }

    @Nonnull
    @Override
    public MType withModifiers(@Nonnull Modifier... modifiers) {
        checkNotNull(modifiers, "modifiers == NULL");
        checkArgument(
                Iterables.all(Arrays.asList(modifiers), Predicates.notNull()),
                "NULL modifier"
        );
        if (modifiers.length == 0) {
            return this;
        } else {
            return new ModifiedType(this, ImmutableList.copyOf(modifiers));
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InternalType that = (InternalType) o;
        return Objects.equals(isStronglyDefined, that.isStronglyDefined) &&
                Objects.equals(name, that.name) &&
                Objects.equals(decl, that.decl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, isStronglyDefined, decl);
    }
}
