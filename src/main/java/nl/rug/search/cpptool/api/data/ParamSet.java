package nl.rug.search.cpptool.api.data;

import javax.annotation.Nonnull;

public interface ParamSet {

    @Nonnull
    Iterable<Variable> params();
}
