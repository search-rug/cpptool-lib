package nl.rug.search.cpptool.runtime.data;

import com.google.common.base.MoreObjects;
import nl.rug.search.cpptool.api.DeclContext;
import nl.rug.search.cpptool.api.data.ParentContext;
import nl.rug.search.cpptool.runtime.impl.DynamicLookup;
import nl.rug.search.cpptool.runtime.mutable.MDeclContext;

import javax.annotation.Nonnull;

public class ParentContextData implements ParentContext {
    private final DynamicLookup<MDeclContext> context;

    public ParentContextData(MDeclContext context) {
        this.context = context.ref();
    }

    @Nonnull
    public static ParentContext build(MDeclContext context) {
        return new ParentContextData(context);
    }

    @Nonnull
    @Override
    public DeclContext parent() {
        return this.context.get();
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper("Parent")
                .add("context", context.get())
                .toString();
    }
}
