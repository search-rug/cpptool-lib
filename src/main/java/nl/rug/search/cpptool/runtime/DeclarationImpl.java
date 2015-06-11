package nl.rug.search.cpptool.runtime;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import nl.rug.search.cpptool.api.DeclType;
import nl.rug.search.cpptool.api.Declaration;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

import static nl.rug.search.cpptool.runtime.util.Debug.NYI;

public class DeclarationImpl implements Declaration {
    private final static LoadingCache<Class<?>, Iterable<Class<?>>> extendedDataMapping = CacheBuilder.newBuilder()
            .build(new CacheLoader<Class<?>, Iterable<Class<?>>>() {
                @Override
                public Iterable<Class<?>> load(Class<?> key) throws Exception {
                    final ImmutableSet.Builder<Class<?>> builder = ImmutableSet.builder();
                    builder.add(key);

                    if (key.isAnnotationPresent(ExtendedData.class)) {
                        final ExtendedData annotation = key.getAnnotation(ExtendedData.class);
                        builder.addAll(Arrays.asList(annotation.value()));
                    }

                    return builder.build();
                }
            });
    private final Map<Class<?>, Object> data = Maps.newIdentityHashMap();

    @Nonnull
    @Override
    public DeclType declarationType() {
        throw NYI();
    }

    @Nonnull
    @Override
    public <T> Optional<T> data(@Nonnull Class<T> dataClass) {
        return Optional.ofNullable(data.get(dataClass)).map(dataClass::cast);
    }

    public <T> void set(Class<T> dataClass, T data) {
        extendedDataMapping.getUnchecked(dataClass).forEach(cls -> this.data.put(cls, data));
    }
}
