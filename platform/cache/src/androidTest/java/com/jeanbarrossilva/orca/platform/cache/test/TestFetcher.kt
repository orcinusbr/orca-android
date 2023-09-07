package com.jeanbarrossilva.orca.platform.cache.test

import com.jeanbarrossilva.orca.platform.cache.Fetcher

internal class TestFetcher : Fetcher<Char>() {
    override suspend fun onFetch(key: String): Char {
        return FETCHED[key.toInt()]
    }

    companion object {
        const val FETCHED = "Hello, world!"
    }
}
