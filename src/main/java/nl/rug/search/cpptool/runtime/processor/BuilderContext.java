package nl.rug.search.cpptool.runtime.processor;

import nl.rug.search.cpptool.api.DeclType;
import nl.rug.search.cpptool.api.data.CxxRecordParent;
import nl.rug.search.cpptool.api.data.Location;
import nl.rug.search.cpptool.proto.Classes;
import nl.rug.search.cpptool.runtime.data.LocationData;
import nl.rug.search.cpptool.runtime.impl.DynamicLookup;
import nl.rug.search.cpptool.runtime.mutable.MDeclaration;
import nl.rug.search.cpptool.runtime.mutable.MSourceFile;
import nl.rug.search.cpptool.runtime.mutable.MType;
import nl.rug.search.cpptool.proto.Base;

import javax.annotation.Nonnull;
import java.util.Optional;

import static nl.rug.search.cpptool.runtime.util.Coerce.coerce;

public interface BuilderContext {
    int DEFAULT_DEFER_PRIORITY = 1000;

    @Nonnull
    MDeclaration createDeclaration(final @Nonnull Base.ScopedName name, final @Nonnull DeclType type);

    @Nonnull
    MDeclaration createIsolatedContext(final @Nonnull Base.IsolatedContextDefinition contextDefinition);

    @Nonnull
    MType findType(final @Nonnull Base.Type type);

    @Nonnull
    DynamicLookup<MSourceFile> file(final @Nonnull String filePath);

    @Nonnull
    DynamicLookup<MDeclaration> findDeclaration(final @Nonnull Base.ScopedName name);

    @Nonnull
    DynamicLookup<MDeclaration> findDeclaration(final @Nonnull Base.Type type);

    void switchInputFile(final @Nonnull String filePath);

    void createTypeMapping(final @Nonnull Base.TypeDefinition typeDefinition);

    void updateTypeMapping(final @Nonnull Base.Type type, final @Nonnull MDeclaration decl);

    @Nonnull
    default Location toLocation(final @Nonnull Base.SourceRange location) {
        return LocationData.build(coerce(file(location.getFile())), location);
    }

    default void defer(final @Nonnull Runnable deferrable) {
        defer(deferrable, DEFAULT_DEFER_PRIORITY);
    }

    void defer(final @Nonnull Runnable deferrable, int priority);

    @Nonnull
    default Optional<Location> toLocation(final @Nonnull Base.SourceRange location, final boolean isSet) {
        if (isSet) {
            return Optional.of(toLocation(location));
        } else {
            return Optional.empty();
        }
    }
}
