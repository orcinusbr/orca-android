package com.jeanbarrossilva.orca.core.mastodon.feed.profile.search.cache.storage

import com.jeanbarrossilva.orca.core.feed.profile.search.ProfileSearchResult
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.cache.storage.MastodonProfileEntityDao
import com.jeanbarrossilva.orca.platform.cache.Storage
import com.jeanbarrossilva.orca.std.imageloader.ImageLoader
import java.net.URL
import kotlinx.coroutines.flow.first

/**
 * [Storage] for [ProfileSearchResult]s.
 *
 * @param avatarLoaderProvider [ImageLoader.Provider] that provides the [ImageLoader] by which the
 *   [ProfileSearchResult]'s avatar will be loaded from a [URL].
 * @param entityDao [MastodonProfileEntityDao] that will perform SQL transactions on
 *   [Mastodon profile search result entities][MastodonProfileSearchResultEntity].
 */
internal class MastodonProfileSearchResultsStorage(
  private val avatarLoaderProvider: ImageLoader.Provider<URL>,
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
