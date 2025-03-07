/*
 * Copyright © 2023–2025 Orcinus
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

package br.com.orcinus.orca.core.mastodon.feed.profile.cache.storage

import br.com.orcinus.orca.core.feed.profile.Profile
import br.com.orcinus.orca.core.feed.profile.post.Post
import br.com.orcinus.orca.core.mastodon.feed.profile.MastodonProfile
import br.com.orcinus.orca.core.mastodon.feed.profile.MastodonProfilePostPaginator
import br.com.orcinus.orca.core.mastodon.feed.profile.type.editable.MastodonEditableProfile
import br.com.orcinus.orca.core.mastodon.feed.profile.type.followable.MastodonFollowableProfile
import br.com.orcinus.orca.core.mastodon.instance.requester.Requester
import br.com.orcinus.orca.platform.cache.Storage
import br.com.orcinus.orca.std.image.ImageLoader
import br.com.orcinus.orca.std.image.SomeImageLoaderProvider
import java.net.URI
import kotlinx.coroutines.flow.first

/**
 * [Storage] for [Profile]s.
 *
 * @property requester [Requester] by which a [MastodonEditableProfile]'s editing requests are
 *   performed and a [MastodonFollowableProfile]'s follow status is obtained.
 * @property avatarLoaderProvider [ImageLoader.Provider] that provides the [ImageLoader] by which
 *   the [Profile]'s avatar will be loaded from a [URI].
 * @property postPaginatorProvider [MastodonProfilePostPaginator.Provider] by which a
 *   [MastodonProfilePostPaginator] for paginating through a [MastodonProfile]'s [Post]s will be
 *   provided.
 * @property entityDao [MastodonProfileEntityDao] that will perform SQL transactions on
 *   [Mastodon profile entities][MastodonProfileEntity].
 */
internal class MastodonProfileStorage(
  private val requester: Requester<*>,
  private val avatarLoaderProvider: SomeImageLoaderProvider<URI>,
  private val postPaginatorProvider: MastodonProfilePostPaginator.Provider,
  private val entityDao: MastodonProfileEntityDao
) : Storage<Profile>() {
  override suspend fun onStore(key: String, value: Profile) {
    val entity = value.toMastodonProfileEntity().copy(id = key)
    entityDao.insert(entity)
  }

  override suspend fun onContains(key: String): Boolean {
    return entityDao.count(key) > 0
  }

  override suspend fun onGet(key: String): Profile {
    return entityDao
      .selectByID(key)
      .first()
      .toProfile(requester, avatarLoaderProvider, entityDao, postPaginatorProvider)
  }

  override suspend fun onRemove(key: String) {
    entityDao.delete(key)
  }

  override suspend fun onClear() {
    entityDao.deleteAll()
  }
}
