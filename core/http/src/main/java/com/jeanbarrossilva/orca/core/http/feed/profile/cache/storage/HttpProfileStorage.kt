package com.jeanbarrossilva.orca.core.http.feed.profile.cache.storage

import com.jeanbarrossilva.orca.core.feed.profile.Profile
import com.jeanbarrossilva.orca.core.feed.profile.toot.Toot
import com.jeanbarrossilva.orca.core.http.feed.profile.HttpProfile
import com.jeanbarrossilva.orca.core.http.feed.profile.HttpProfileTootPaginator
import com.jeanbarrossilva.orca.platform.cache.Storage
import com.jeanbarrossilva.orca.std.imageloader.ImageLoader
import java.net.URL
import kotlinx.coroutines.flow.first

/**
 * [Storage] for [Profile]s.
 *
 * @param avatarLoaderProvider [ImageLoader.Provider] that provides the [ImageLoader] by which the
 *   [Profile]'s avatar will be loaded from a [URL].
 * @param tootPaginatorProvider [HttpProfileTootPaginator.Provider] by which a
 *   [HttpProfileTootPaginator] for paginating through a [HttpProfile]'s [Toot]s will be provided.
 * @param entityDao [HttpProfileEntityDao] that will perform SQL transactions on
 *   [HTTP profile entities][HttpProfileEntity].
 */
internal class HttpProfileStorage(
  private val avatarLoaderProvider: ImageLoader.Provider<URL>,
  private val tootPaginatorProvider: HttpProfileTootPaginator.Provider,
  private val entityDao: HttpProfileEntityDao
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
      .toProfile(avatarLoaderProvider, entityDao, tootPaginatorProvider)
  }

  override suspend fun onRemove(key: String) {
    entityDao.delete(key)
  }

  override suspend fun onClear() {
    entityDao.deleteAll()
  }
}
