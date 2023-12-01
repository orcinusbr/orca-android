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

package com.jeanbarrossilva.orca.core.feed.profile.search

import com.jeanbarrossilva.orca.core.feed.profile.account.Account
import com.jeanbarrossilva.orca.std.imageloader.SomeImageLoader
import java.io.Serializable
import java.net.URL

/**
 * Result of a profile search.
 *
 * @param id Unique identifier.
 * @param account Unique identifier within an instance.
 * @param avatarLoader [ImageLoader] that loads the avatar.
 * @param name Name to be displayed.
 * @param url [URL] that leads to the profile.
 */
data class ProfileSearchResult(
  val id: String,
  val account: Account,
  val avatarLoader: SomeImageLoader,
  val name: String,
  val url: URL
) : Serializable {
  companion object
}
