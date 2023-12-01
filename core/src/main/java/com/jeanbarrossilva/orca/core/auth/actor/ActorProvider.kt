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

package com.jeanbarrossilva.orca.core.auth.actor

/** Provides an [Actor] through [provide]. */
abstract class ActorProvider {
  /** Provides an [Actor]. */
  suspend fun provide(): Actor {
    return retrieve()
  }

  /**
   * Remembers the given [actor] so that it can be retrieved later.
   *
   * @see retrieve
   */
  @Suppress("FunctionName")
  internal suspend fun _remember(actor: Actor) {
    remember(actor)
  }

  /**
   * Remembers the given [actor] so that it can be retrieved later.
   *
   * @see retrieve
   */
  protected abstract suspend fun remember(actor: Actor)

  /**
   * Retrieves a remembered [Actor].
   *
   * @see remember
   */
  protected abstract suspend fun retrieve(): Actor

  companion object
}
