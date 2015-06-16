package nl.rug.search.cpptool.runtime.mutable;

import nl.rug.search.cpptool.api.DeclContext;
import nl.rug.search.cpptool.runtime.impl.DynamicLookup;

public interface MDeclContext extends DeclContext {
    DynamicLookup<MDeclaration> getDeclaration(String name);
}
