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

import br.com.orcinus.orca.core.feed.profile.search.ProfileSearchResult
import br.com.orcinus.orca.core.mastodon.feed.profile.cache.storage.MastodonProfileEntityDao
import br.com.orcinus.orca.platform.cache.Storage
import br.com.orcinus.orca.std.image.ImageLoader
import br.com.orcinus.orca.std.image.SomeImageLoaderProvider
import java.net.URI
import kotlinx.coroutines.flow.first

/**
 * [Storage] for [ProfileSearchResult]s.
 *
 * @param avatarLoaderProvider [ImageLoader.Provider] that provides the [ImageLoader] by which the
 *   [ProfileSearchResult]'s avatar will be loaded from a [URI].
 * @param entityDao [MastodonProfileEntityDao] that will perform SQL transactions on
 *   [Mastodon profile search result entities][MastodonProfileSearchResultEntity].
 */
internal class MastodonProfileSearchResultsStorage(
  private val avatarLoaderProvider: SomeImageLoaderProvider<URI>,
  private val entityDao: MastodonProfileSearchResultEntityDao
) : Storage<List<ProfileSearchResult>>() {
  override suspend fun onStore(key: String, value: List<ProfileSearchResult>) {
    val entities = value.map { it.toMastodonProfileSearchResultEntity(key) }
    entityDao.insert(entities)
  }

  override suspend fun onContains(key: String): Boolean {
    return entityDao.count(key) > 0
  }

  override suspend fun onGet(key: String): List<ProfileSearchResult> {
    return entityDao.selectByQuery(key).first().map {
      it.toProfileSearchResult(avatarLoaderProvider)
    }
  }

  override suspend fun onRemove(key: String) {
    entityDao.delete(key)
  }

  override suspend fun onClear() {
    entityDao.deleteAll()
  }
}
