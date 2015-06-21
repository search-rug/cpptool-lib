package nl.rug.search.cpptool.api.data;

import nl.rug.search.cpptool.api.DeclContext;
import nl.rug.search.cpptool.runtime.mutable.MDeclContext;

import javax.annotation.Nonnull;

public interface ParentContext {
    @Nonnull
    DeclContext parent();
}
