package com.jeanbarrossilva.orca.core.http.feed.profile.toot.cache.storage

import com.jeanbarrossilva.orca.core.feed.profile.Profile
import com.jeanbarrossilva.orca.core.feed.profile.toot.Toot
import com.jeanbarrossilva.orca.core.feed.profile.toot.content.Content
import com.jeanbarrossilva.orca.core.feed.profile.toot.content.highlight.Highlight
import com.jeanbarrossilva.orca.core.http.feed.profile.toot.cache.storage.style.HttpStyleEntity
import com.jeanbarrossilva.orca.core.http.feed.profile.toot.cache.storage.style.HttpStyleEntityDao
import com.jeanbarrossilva.orca.core.http.feed.profile.toot.cache.storage.style.toHttpStyleEntity
import com.jeanbarrossilva.orca.platform.cache.Cache
import com.jeanbarrossilva.orca.platform.cache.Storage
import com.jeanbarrossilva.orca.std.imageloader.ImageLoader
import java.net.URL

/**
 * [Storage] for [Toot]s.
 *
 * @param profileCache [Cache] of [Profile]s with which [HTTP toot entities][HttpTootEntity] will be
 *   converted to [Toot]s.
 * @param tootEntityDao [HttpStyleEntityDao] that will perform SQL transactions on
 *   [HTTP toot entities][HttpTootEntity].
 * @param styleEntityDao [HttpStyleEntityDao] for inserting and deleting
 *   [HTTP style entities][HttpStyleEntity].
 * @param coverLoaderProvider [ImageLoader.Provider] that provides the [ImageLoader] by which a
 *   [Toot]'s [content][Toot.content]'s [highlight][Content.highlight]'s
 *   [headline][Highlight.headline] cover will be loaded from a [URL].
 */
internal class HttpTootStorage(
  private val profileCache: Cache<Profile>,
  private val tootEntityDao: HttpTootEntityDao,
  private val styleEntityDao: HttpStyleEntityDao,
  private val coverLoaderProvider: ImageLoader.Provider<URL>
) : Storage<Toot>() {
  override suspend fun onStore(key: String, value: Toot) {
    val tootEntity = HttpTootEntity.from(value)
    val styleEntities = value.content.text.styles.map { it.toHttpStyleEntity(value.id) }
    tootEntityDao.insert(tootEntity)
    styleEntityDao.insert(styleEntities)
  }

  override suspend fun onContains(key: String): Boolean {
    return tootEntityDao.count(key) > 0
  }

  override suspend fun onGet(key: String): Toot {
    return tootEntityDao.selectByID(key).toToot(profileCache, tootEntityDao, coverLoaderProvider)
  }

  override suspend fun onRemove(key: String) {
    val mentionEntities = styleEntityDao.selectByTootID(key)
    styleEntityDao.delete(mentionEntities)
    tootEntityDao.delete(key)
  }

  override suspend fun onClear() {
    tootEntityDao.deleteAll()
  }
}
