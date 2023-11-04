package com.jeanbarrossilva.orca.core.http.feed.profile.search.cache.storage

import com.jeanbarrossilva.orca.core.feed.profile.search.ProfileSearchResult
import com.jeanbarrossilva.orca.core.http.feed.profile.cache.storage.HttpProfileEntityDao
import com.jeanbarrossilva.orca.platform.cache.Storage
import com.jeanbarrossilva.orca.std.imageloader.ImageLoader
import java.net.URL
import kotlinx.coroutines.flow.first

/**
 * [Storage] for [ProfileSearchResult]s.
 *
 * @param avatarLoaderProvider [ImageLoader.Provider] that provides the [ImageLoader] by which the
 *   [ProfileSearchResult]'s avatar will be loaded from a [URL].
 * @param entityDao [HttpProfileEntityDao] that will perform SQL transactions on
 *   [HTTP profile search result entities][HttpProfileSearchResultEntity].
 */
internal class HttpProfileSearchResultsStorage(
  private val avatarLoaderProvider: ImageLoader.Provider<URL>,
  private val entityDao: HttpProfileSearchResultEntityDao
) : Storage<List<ProfileSearchResult>>() {
  override suspend fun onStore(key: String, value: List<ProfileSearchResult>) {
    val entities = value.map { it.toProfileSearchResultEntity(key) }
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
