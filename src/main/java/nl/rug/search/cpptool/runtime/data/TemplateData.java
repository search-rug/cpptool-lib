package nl.rug.search.cpptool.runtime.data;

import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import nl.rug.search.cpptool.api.Declaration;
import nl.rug.search.cpptool.api.Type;
import nl.rug.search.cpptool.api.data.Template;
import nl.rug.search.cpptool.api.data.TemplateParameter;
import nl.rug.search.cpptool.runtime.impl.DynamicLookup;
import nl.rug.search.cpptool.runtime.mutable.MType;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;

public class TemplateData implements Template {
    private final List<DynamicLookup<MType>> specs;
    private final List<TemplateParameter> params = Lists.newLinkedList();

    public TemplateData(List<DynamicLookup<MType>> specs) {
        this.specs = specs;
    }

    public static TemplateData get(Declaration decl) {
        return (TemplateData) decl.dataUnchecked(Template.class);
    }

    public static TemplateData build(List<DynamicLookup<MType>> specializations) {
        return new TemplateData(ImmutableList.copyOf(specializations));
    }

    @Nonnull
    @Override
    public Iterable<Type> specializations() {
        return Lists.transform(specs, DynamicLookup::get);
    }

    @Nonnull
    @Override
    public Iterable<TemplateParameter> params() {
        return Collections.unmodifiableList(params);
    }

    public void addParam(DynamicLookup<MType> type) {
        this.params.add(new Param(type));
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper("Template")
                .add("specs", Iterables.toString(specializations()))
                .add("params", Iterables.toString(params()))
                .toString();
    }

    private static class Param implements TemplateParameter {
        private final DynamicLookup<MType> type;

        public Param(DynamicLookup<MType> type) {
            this.type = type;
        }

        @Nonnull
        @Override
        public Type type() {
            return type.get();
        }

        @Override
        public String toString() {
            return MoreObjects.toStringHelper("Param")
                    .add("type", type)
                    .add("token", type().name())
                    .toString();
        }
    }
}
