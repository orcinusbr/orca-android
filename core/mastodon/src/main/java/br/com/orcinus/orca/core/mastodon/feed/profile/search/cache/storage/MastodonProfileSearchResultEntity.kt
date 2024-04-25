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

package br.com.orcinus.orca.core.mastodon.feed.profile.search.cache.storage

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import br.com.orcinus.orca.core.feed.profile.account.Account
import br.com.orcinus.orca.core.feed.profile.search.ProfileSearchResult
import br.com.orcinus.orca.std.image.ImageLoader
import br.com.orcinus.orca.std.image.SomeImageLoaderProvider
import java.net.URI

/**
 * Primitive information to be persisted about a [ProfileSearchResult].
 *
 * @param query Search query to which the [ProfileSearchResult] is associated.
 * @param id Unique identifier.
 * @param account [String] representation of the [MastodonProfileSearchResultEntity]'s
 *   [account][MastodonProfileSearchResultEntity.account].
 * @param avatarURI URI [String] that leads to the avatar image.
 * @param name Name to be displayed.
 * @param uri URI [String] that leads to the owner.
 */
@Entity(tableName = "profile_search_results")
data class MastodonProfileSearchResultEntity(
  @PrimaryKey internal val query: String,
  internal val id: String,
  internal val account: String,
  @ColumnInfo(name = "avatar_uri") internal val avatarURI: String,
  internal val name: String,
  internal val uri: String
) {
  /**
   * Converts this [MastodonProfileSearchResultEntity] into a [ProfileSearchResult].
   *
   * @param avatarLoaderProvider [ImageLoader.Provider] that provides the [ImageLoader] by which the
   *   [ProfileSearchResult]'s avatar will be loaded from a [URI].
   */
  internal fun toProfileSearchResult(
    avatarLoaderProvider: SomeImageLoaderProvider<URI>
  ): ProfileSearchResult {
    val account = Account.of(account, fallbackDomain = "mastodon.social")
    val avatarURI = URI(avatarURI)
    val avatarLoader = avatarLoaderProvider.provide(avatarURI)
    val uri = URI(uri)
    return ProfileSearchResult(id, account, avatarLoader, name, uri)
  }
}
