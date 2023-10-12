package com.jeanbarrossilva.orca.platform.cache.test

import com.jeanbarrossilva.orca.platform.cache.Storage

internal class TestStorage : Storage<Char>() {
  private var stored = ""

  override suspend fun onStore(key: String, value: Char) {
    val index = key.toInt()
    stored = buildString {
      append(stored)
      insert(index, value)
    }
  }

  override suspend fun onContains(key: String): Boolean {
    return stored.isNotEmpty() && key.toInt() in 0..stored.lastIndex
  }

  override suspend fun onGet(key: String): Char {
    return stored[key.toInt()]
  }

  override suspend fun onRemove(key: String) {
    val index = key.toInt()
    stored = stored.substring(0..index) + stored.substring(index.inc()..stored.length)
  }

  override suspend fun onClear() {
    stored = ""
  }
}
