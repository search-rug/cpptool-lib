package nl.rug.search.cpptool.runtime.util;

import com.google.common.cache.CacheLoader;

import java.util.function.Function;

public class FunctionalCacheLoader<K, V> extends CacheLoader<K, V> {
    private final Function<K, V> func;

    public FunctionalCacheLoader(Function<K, V> func) {
        this.func = func;
    }

    @Override
    public V load(K key) throws Exception {
        return this.func.apply(key);
    }
}
