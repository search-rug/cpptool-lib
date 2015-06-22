package nl.rug.search.cpptool.api;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import nl.rug.search.cpptool.api.data.CxxFunction;
import nl.rug.search.cpptool.api.data.CxxRecord;
import nl.rug.search.cpptool.api.data.CxxVariable;

import java.util.Set;

public class T {
    private static final Set<Class<?>> CXX_MEMBERS = ImmutableSet.of(CxxFunction.class, CxxVariable.class);

    private T() {
        //This class shouldn't be instantiated.
        throw new ExceptionInInitializerError(T.class.getCanonicalName() + " cannot be instantiated.");
    }

    public static boolean isClass(Declaration d) {
        return d.declarationType() == DeclType.RECORD && d.has(CxxRecord.class);
    }

    public static boolean isClassMember(Declaration d) {
        return Iterables.any(CXX_MEMBERS, d::has);
    }
}
