package com.jeanbarrossilva.orca.platform.cache

/** Fetches values from an external source (normally the network) through [onFetch]. **/
abstract class Fetcher<T> {
    /**
     * Fetches a value associated to the given [key].
     *
     * @param key Unique identifier of the value to be fetched.
     **/
    internal suspend fun fetch(key: String): T {
        return onFetch(key)
    }

    /**
     * Fetches a value associated to the given [key].
     *
     * @param key Unique identifier of the value to be fetched.
     **/
    protected abstract suspend fun onFetch(key: String): T
}
