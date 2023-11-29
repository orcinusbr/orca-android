package com.jeanbarrossilva.orca.core.mastodon.feed.profile.cache.storage

import com.jeanbarrossilva.orca.core.feed.profile.Profile
import com.jeanbarrossilva.orca.core.feed.profile.post.Post
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.MastodonProfile
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.MastodonProfilePostPaginator
import com.jeanbarrossilva.orca.platform.cache.Storage
import com.jeanbarrossilva.orca.std.imageloader.ImageLoader
import java.net.URL
import kotlinx.coroutines.flow.first

/**
 * [Storage] for [Profile]s.
 *
 * @param avatarLoaderProvider [ImageLoader.Provider] that provides the [ImageLoader] by which the
 *   [Profile]'s avatar will be loaded from a [URL].
 * @param postPaginatorProvider [MastodonProfilePostPaginator.Provider] by which a
 *   [MastodonProfilePostPaginator] for paginating through a [MastodonProfile]'s [Post]s will be
 *   provided.
 * @param entityDao [MastodonProfileEntityDao] that will perform SQL transactions on
 *   [Mastodon profile entities][MastodonProfileEntity].
 */
internal class MastodonProfileStorage(
  private val avatarLoaderProvider: ImageLoader.Provider<URL>,
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
      .toProfile(avatarLoaderProvider, entityDao, postPaginatorProvider)
  }

  override suspend fun onRemove(key: String) {
    entityDao.delete(key)
  }

  override suspend fun onClear() {
    entityDao.deleteAll()
  }
}
