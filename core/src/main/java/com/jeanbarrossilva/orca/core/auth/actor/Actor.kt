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

/** Agent that can perform operations throughout the application. */
sealed interface Actor {
  /** Unknown [Actor] that has restricted access. */
  object Unauthenticated : Actor

  /**
   * [Actor] that's been properly authenticated and can use the application normally.
   *
   * @param id Unique identifier within Mastodon.
   * @param accessToken Access token resulted from the authentication.
   */
  data class Authenticated(val id: String, val accessToken: String) : Actor {
    companion object
  }
}
