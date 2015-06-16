package nl.rug.search.cpptool.runtime.data;

import com.google.common.base.MoreObjects;
import nl.rug.search.cpptool.api.DeclContext;
import nl.rug.search.cpptool.api.data.ContextHolder;
import nl.rug.search.cpptool.runtime.mutable.MDeclContext;

import javax.annotation.Nonnull;

public class ContextHolderData implements ContextHolder {
    private final MDeclContext context;

    private ContextHolderData(MDeclContext context) {
        this.context = context;
    }

    public static ContextHolderData build(MDeclContext context) {
        return new ContextHolderData(context);
    }

    @Nonnull
    @Override
    public DeclContext context() {
        return this.context;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper("ContextHolder")
                .add("context", context)
                .toString();
    }
}
