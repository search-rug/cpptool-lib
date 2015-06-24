package nl.rug.search.cpptool.runtime.util;

import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Sets;
import nl.rug.search.cpptool.api.DeclContainer;
import nl.rug.search.cpptool.api.DeclContext;
import nl.rug.search.cpptool.api.Declaration;
import nl.rug.search.cpptool.api.SourceFile;
import nl.rug.search.cpptool.api.util.ContextTools;

import java.util.Optional;
import java.util.Set;

public class StateValidator {
    public static void validateGraph(DeclContainer container) {
        final Set<DeclContext> primaryContexts = Sets.newIdentityHashSet();
        Iterables.addAll(primaryContexts, allContexts(container.context()));
        final Set<Declaration> primaryDecls = Sets.newIdentityHashSet();
        Iterables.addAll(primaryDecls, ContextTools.traverseDeclarations(container.context()));

        // Assertion: None of the contexts found in filetrees should be found in the primary tree
        final Set<DeclContext> duplicateContexts = FluentIterable
                .from(container.inputFiles())
                .transform(SourceFile::localContext)
                .transform(Optional::get)
                .transformAndConcat(StateValidator::allContexts)
                .filter(primaryContexts::contains)
                .toSet(); //Check if they are a part of the primary tree

        if (duplicateContexts.size() > 0) {
            System.err.printf("Assertion failed, duplicate contexts (%d/%d):%n",
                    duplicateContexts.size(),
                    primaryContexts.size());
            duplicateContexts.forEach(System.err::println);
        }

        // Assertion: All declarations should link back to the primary tree
        final Set<Declaration> danglingDecls = FluentIterable
                .from(container.inputFiles())
                .transform(SourceFile::localContext)
                .transform(Optional::get)
                .transformAndConcat(ContextTools::traverseDeclarations)
                .filter((d) -> !(primaryDecls.contains(d) && primaryContexts.contains(d.parentContext())))
                .toSet(); //Check if they are not in the primary tree

        if (danglingDecls.size() > 0) {
            System.err.printf("Assertion failed, dangling declarations (%d/%d):%n",
                    danglingDecls.size(),
                    primaryDecls.size());
            traceTrees(danglingDecls, createRootSet(container), primaryContexts);
        }

        System.err.printf("Total contexts: %d%n", primaryContexts.size());
        System.err.printf("Total declarations: %d%n", primaryDecls.size());

        System.err.flush();
        System.out.flush();
    }

    private static void traceTrees(Set<Declaration> decls, Set<DeclContext> rootSet, Set<DeclContext> primaryContexts) {
        for (Declaration d : decls) {
            System.err.print(d.name().orElse("{n/a}"));
            DeclContext context = d.parentContext();
            for (; ; ) {
                if (rootSet.contains(context)) break;
                System.err.printf(" -> %s (in-tree=%b)",
                        context.name().orElse("{lambda}"),
                        primaryContexts.contains(context));
                context = context.parent();
            }
            System.err.println();
        }
    }

    private static Set<DeclContext> createRootSet(DeclContainer container) {
        final Set<DeclContext> roots = Sets.newIdentityHashSet();
        roots.add(container.context());
        FluentIterable.from(container.inputFiles())
                .transform(SourceFile::localContext)
                .transform(Optional::get)
                .forEach(roots::add);

        return roots;
    }

    public static void validateState(DeclContainer container) {
        final Set<Declaration> it = ImmutableSet.copyOf(ContextTools.validateState(container.context()));
        if (it.size() > 0) {
            System.err.println("Invalid declarations:");
            it.forEach(System.err::println);
        }
    }

    private static Iterable<DeclContext> allContexts(DeclContext root) {
        return FluentIterable.from(root.children()).transformAndConcat(StateValidator::allContexts).append(root);
    }
}
