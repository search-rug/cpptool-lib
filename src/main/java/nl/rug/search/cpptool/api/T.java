package nl.rug.search.cpptool.api;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import nl.rug.search.cpptool.api.data.CxxFunction;
import nl.rug.search.cpptool.api.data.CxxRecord;
import nl.rug.search.cpptool.api.data.CxxVariable;
import nl.rug.search.cpptool.api.data.Record;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Utility filtering functions to filter down declarations to a desired subset.
 * <br />
 * Functions that return booleans can be used through the Java8 reference syntax:
 * {@code filter(T::isCRecord)}
 * <br />
 * Functions that return SuperPredicates need to be called with some configuration data and then passed to the filter:
 * {@code filter(T.filterDeclType(DeclType.RECORD))}
 *
 * @author David van Leusen <J.D.van.leusen@student.rug.nl>
 * @since 2015-06-25
 */
public class T {
    private static final ComponentFilter CXX_MEMBERS = new ComponentFilter(CxxFunction.class, CxxVariable.class);

    private T() {
        //This class shouldn't be instantiated.
        throw new ExceptionInInitializerError(T.class.getCanonicalName() + " cannot be instantiated.");
    }

    public static boolean isCxxClass(Declaration d) {
        return d.declarationType() == DeclType.RECORD && d.has(CxxRecord.class);
    }

    public static boolean isClassMember(Declaration d) {
        return CXX_MEMBERS.test(d);
    }

    public static boolean isFunction(Declaration d) {
        return d.declarationType() == DeclType.FUNCTION;
    }

    public static boolean isVariable(Declaration d) {
        return d.declarationType() == DeclType.VARIABLE;
    }

    public static boolean isCRecord(Declaration d) {
        return d.declarationType() == DeclType.RECORD && d.has(Record.class) && !d.has(CxxRecord.class);
    }

    @Nonnull
    public static SuperPredicate<Declaration> filterDeclType(DeclType dt) {
        return new DeclTypeFilter(checkNotNull(dt));
    }

    @Nonnull
    public static SuperPredicate<Declaration> filterComponents(Class<?>... componentClasses) {
        return new ComponentFilter(componentClasses);
    }

    private static class ComponentFilter extends SuperPredicate<Declaration> {
        private final ImmutableSet<Class<?>> filterSet;

        public ComponentFilter(Class<?>... dataClasses) {
            checkArgument(FluentIterable.of(dataClasses).allMatch(Predicates.notNull()));
            this.filterSet = ImmutableSet.copyOf(dataClasses);
        }

        @Override
        public boolean test(Declaration declaration) {
            return Iterables.any(filterSet, declaration::has);
        }
    }

    private static class DeclTypeFilter extends SuperPredicate<Declaration> {
        private final DeclType dt;

        public DeclTypeFilter(DeclType dt) {
            this.dt = dt;
        }

        @Override
        public boolean test(Declaration declaration) {
            return declaration.declarationType() == dt;
        }
    }

    /**
     * Predicate that is compatible with both Java8's predicate interface and Guava's predicate interface.
     */
    public static abstract class SuperPredicate<T> implements Predicate<T>, java.util.function.Predicate<T> {

        @Override
        public boolean apply(T input) {
            return test(input);
        }
    }
}
