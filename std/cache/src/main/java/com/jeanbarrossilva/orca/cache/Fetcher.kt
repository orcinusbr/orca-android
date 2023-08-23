package com.jeanbarrossilva.orca.cache

/** Fetches values from an external source (normally the network) through [onFetch]. **/
abstract class Fetcher<K, V> {
    /**
     * Fetches a value associated to the given [key].
     *
     * @param key Unique identifier of the value to be fetched.
     **/
    internal suspend fun fetch(key: K): V {
        return onFetch(key)
    }

    /**
     * Fetches a value associated to the given [key].
     *
     * @param key Unique identifier of the value to be fetched.
     **/
    protected abstract suspend fun onFetch(key: K): V
}
