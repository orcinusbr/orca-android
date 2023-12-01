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

package com.jeanbarrossilva.orca.core.feed.profile.post.content

import com.jeanbarrossilva.orca.std.buildable.Buildable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf

/**
 * Mutes and retrieves terms that have been muted.
 *
 * An instance of this class can be created via its factory method.
 */
@Buildable
abstract class TermMuter internal constructor() {
  /**
   * Whether the [content] contains muted terms.
   *
   * @param content [Content] whose terms will be verified.
   */
  suspend fun isMuted(content: Content): Boolean {
    val terms = getTerms().first()
    return terms.any { it in content.text }
  }

  /** Gets the [Flow] to which the muted terms are emitted. */
  open fun getTerms(): Flow<List<String>> {
    return flowOf(emptyList())
  }

  /**
   * Mutes the given [term].
   *
   * @param term Term to be muted.
   */
  open suspend fun mute(term: String) {}

  /**
   * Unmutes the given [term].
   *
   * @param term Term to be unmuted.
   */
  open suspend fun unmute(term: String) {}
}
