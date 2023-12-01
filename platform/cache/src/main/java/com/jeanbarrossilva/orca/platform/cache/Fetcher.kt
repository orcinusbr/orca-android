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

package com.jeanbarrossilva.orca.platform.cache

/** Fetches values from an external source (normally the network) through [onFetch]. */
abstract class Fetcher<T> {
  /**
   * Fetches a value associated to the given [key].
   *
   * @param key Unique identifier of the value to be fetched.
   */
  internal suspend fun fetch(key: String): T {
    return onFetch(key)
  }

  /**
   * Fetches a value associated to the given [key].
   *
   * @param key Unique identifier of the value to be fetched.
   */
  protected abstract suspend fun onFetch(key: String): T
}
