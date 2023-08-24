package com.jeanbarrossilva.orca.platform.cache.test

import com.jeanbarrossilva.orca.platform.cache.Fetcher

internal class TestFetcher : Fetcher<Int, Char>() {
    override suspend fun onFetch(key: Int): Char {
        return FETCHED[key]
    }

    companion object {
        const val FETCHED = "Hello, world!"
    }
}
