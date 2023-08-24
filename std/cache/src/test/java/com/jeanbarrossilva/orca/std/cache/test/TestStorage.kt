package com.jeanbarrossilva.orca.std.cache.test

import com.jeanbarrossilva.orca.std.cache.Storage

internal class TestStorage : Storage<Int, Char>() {
    private var stored = ""

    override suspend fun onStore(key: Int, value: Char) {
        stored = buildString {
            append(stored)
            insert(key, value)
        }
    }

    override suspend fun onContains(key: Int): Boolean {
        return key in 0..stored.lastIndex
    }

    override suspend fun onGet(key: Int): Char {
        return stored[key]
    }

    override suspend fun onRemove(key: Int) {
        stored = stored.substring(key..(key + 1))
    }

    override suspend fun onClear() {
        stored = ""
    }
}
