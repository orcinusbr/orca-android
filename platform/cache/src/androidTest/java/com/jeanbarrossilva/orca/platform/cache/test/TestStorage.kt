/*
 * Copyright © 2023 Orca
 *
 * Licensed under the GNU General Public License, Version 3 (the "License"); you may not use this
 * file except in compliance with the License. You may obtain a copy of the License at
 *
 *                        https://www.gnu.org/licenses/gpl-3.0.html
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
