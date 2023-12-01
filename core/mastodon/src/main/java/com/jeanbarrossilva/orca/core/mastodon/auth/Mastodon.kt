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

package com.jeanbarrossilva.orca.core.mastodon.auth

import com.jeanbarrossilva.orca.core.mastodon.BuildConfig

/** API configuration for authorization and authentication. */
internal object Mastodon {
  /** Identifies Orca amongst all Mastodon clients. */
  @Suppress("SpellCheckingInspection")
  const val CLIENT_ID = "F2Rx9d7C3x45KRVJ9rU4IjIJgrsjzaq74bSLo__VUG0"

  /** Private code. */
  const val CLIENT_SECRET = BuildConfig.mastodonclientSecret

  /** Scopes required by Orca for its functionalities to work properly. */
  const val SCOPES = "read write follow"
}
