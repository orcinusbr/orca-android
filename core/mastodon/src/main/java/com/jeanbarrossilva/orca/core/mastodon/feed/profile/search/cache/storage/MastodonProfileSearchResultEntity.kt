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

package com.jeanbarrossilva.orca.core.mastodon.feed.profile.search.cache.storage

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jeanbarrossilva.orca.core.feed.profile.account.Account
import com.jeanbarrossilva.orca.core.feed.profile.search.ProfileSearchResult
import com.jeanbarrossilva.orca.std.imageloader.ImageLoader
import java.net.URL

/**
 * Primitive information to be persisted about a [ProfileSearchResult].
 *
 * @param query Search query to which the [ProfileSearchResult] is associated.
 * @param id Unique identifier.
 * @param account [String] representation of the [MastodonProfileSearchResultEntity]'s
 *   [account][MastodonProfileSearchResultEntity.account].
 * @param avatarURL URL [String] that leads to the avatar image.
 * @param name Name to be displayed.
 * @param url URL [String] that leads to the owner.
 */
@Entity(tableName = "profile_search_results")
data class MastodonProfileSearchResultEntity(
  @PrimaryKey internal val query: String,
  internal val id: String,
  internal val account: String,
  @ColumnInfo(name = "avatar_url") internal val avatarURL: String,
  internal val name: String,
  internal val url: String
) {
  /**
   * Converts this [MastodonProfileSearchResultEntity] into a [ProfileSearchResult].
   *
   * @param avatarLoaderProvider [ImageLoader.Provider] that provides the [ImageLoader] by which the
   *   [ProfileSearchResult]'s avatar will be loaded from a [URL].
   */
  internal fun toProfileSearchResult(
    avatarLoaderProvider: ImageLoader.Provider<URL>
  ): ProfileSearchResult {
    val account = Account.of(account, fallbackDomain = "mastodon.social")
    val avatarURL = URL(avatarURL)
    val avatarLoader = avatarLoaderProvider.provide(avatarURL)
    val url = URL(url)
    return ProfileSearchResult(id, account, avatarLoader, name, url)
  }
}
