package nl.rug.search.cpptool.runtime.data;

import com.google.common.base.MoreObjects;
import com.google.common.collect.Lists;
import nl.rug.search.cpptool.api.Declaration;
import nl.rug.search.cpptool.api.data.ParamSet;
import nl.rug.search.cpptool.api.data.Variable;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;

public class ParamSetData implements ParamSet {
    private final List<Variable> vars = Lists.newLinkedList();

    public static ParamSetData build() {
        return new ParamSetData();
    }

    public static ParamSetData hackyAccess(final Declaration decl) {
        final ParamSet params = decl.dataUnchecked(ParamSet.class);
        return (ParamSetData) params;
    }

    @Nonnull
    @Override
    public Iterable<Variable> params() {
        return Collections.unmodifiableList(vars);
    }

    public void addVar(Variable var) {
        this.vars.add(var);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper("Param")
                .add("params", vars)
                .toString();
    }
}
