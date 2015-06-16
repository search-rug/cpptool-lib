package nl.rug.search.cpptool.runtime.impl;

import com.google.common.base.MoreObjects;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.Maps;
import com.google.common.collect.Ordering;
import nl.rug.search.cpptool.api.DeclType;
import nl.rug.search.cpptool.runtime.ExtendedData;
import nl.rug.search.cpptool.runtime.mutable.MDeclaration;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

class InternalDeclaration implements MDeclaration {
    private final Map<Class<?>, Object> data = Maps.newIdentityHashMap();
    private final DeclType type;

    public InternalDeclaration(DeclType type) {
        this.type = type;
    }

    @Nonnull
    @Override
    public DeclType declarationType() {
        return this.type;
    }

    @Nonnull
    @Override
    public <T> Optional<T> data(@Nonnull Class<T> dataClass) {
        return Optional.ofNullable(data.get(dataClass)).map(dataClass::cast);
    }

    @Override
    public <T> void insertData(@Nonnull Class<T> dataClass, @Nonnull T data) {
        INNER_DATA_MAPPER.extendedDataMapping.getUnchecked(dataClass).forEach(cls -> this.data.put(cls, data));
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper("Declaration")
                .add("type", type)
                .add("data", ImmutableSortedSet.copyOf(Ordering.arbitrary(), data.values()))
                .toString();
    }

    private interface INNER_DATA_MAPPER {
        LoadingCache<Class<?>, Iterable<Class<?>>> extendedDataMapping = CacheBuilder.newBuilder()
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
    }
}
