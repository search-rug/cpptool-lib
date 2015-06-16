package nl.rug.search.cpptool.runtime;

import com.google.common.collect.ImmutableList;
import nl.rug.search.cpptool.api.DeclContainer;
import nl.rug.search.cpptool.runtime.impl.ContextFactory;
import nl.rug.search.cpptool.runtime.mutable.MDeclContext;

import static nl.rug.search.cpptool.runtime.util.Debug.NYI;

class ResultMerger {
    private final ContextFactory factory = new ContextFactory();
    private final MDeclContext globalContext = factory.createTopContext();
    //TODO: internally track the files that have a declcontext attached

    public ResultMerger feed(PartialResult result) {
        throw NYI();
    }

    public DeclContainer build() {
        //TODO: attach file list
        return factory.build(globalContext, ImmutableList.of());
    }
}
