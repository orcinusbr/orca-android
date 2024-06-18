/*
 * Copyright © 2023–2024 Orcinus
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see https://www.gnu.org/licenses.
 */

package br.com.orcinus.orca.platform.cache.memory

import br.com.orcinus.orca.platform.cache.Storage

internal class InMemoryStorage : Storage<Char>() {
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
