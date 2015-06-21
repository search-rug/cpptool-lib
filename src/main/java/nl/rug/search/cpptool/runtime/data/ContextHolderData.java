package nl.rug.search.cpptool.runtime.data;

import com.google.common.base.MoreObjects;
import nl.rug.search.cpptool.api.DeclContext;
import nl.rug.search.cpptool.api.data.ContextHolder;
import nl.rug.search.cpptool.runtime.impl.DynamicLookup;
import nl.rug.search.cpptool.runtime.mutable.MDeclContext;

import javax.annotation.Nonnull;

public class ContextHolderData implements ContextHolder {
    private final DynamicLookup<MDeclContext> context;

    private ContextHolderData(DynamicLookup<MDeclContext> context) {
        this.context = context;
    }

    public static ContextHolderData build(DynamicLookup<MDeclContext> context) {
        return new ContextHolderData(context);
    }

    @Nonnull
    @Override
    public DeclContext context() {
        return this.context.get();
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper("ContextHolder")
                .add("context", context)
                .toString();
    }
}
