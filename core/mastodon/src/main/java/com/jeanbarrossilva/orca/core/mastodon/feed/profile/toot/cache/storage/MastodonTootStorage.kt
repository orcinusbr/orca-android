package com.jeanbarrossilva.orca.core.mastodon.feed.profile.toot.cache.storage

import com.jeanbarrossilva.orca.core.feed.profile.Profile
import com.jeanbarrossilva.orca.core.feed.profile.toot.Toot
import com.jeanbarrossilva.orca.core.feed.profile.toot.content.Content
import com.jeanbarrossilva.orca.core.feed.profile.toot.content.highlight.Highlight
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.cache.storage.style.MastodonStyleEntity
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.cache.storage.style.MastodonStyleEntityDao
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.cache.storage.style.toHttpStyleEntity
import com.jeanbarrossilva.orca.platform.cache.Cache
import com.jeanbarrossilva.orca.platform.cache.Storage
import com.jeanbarrossilva.orca.std.imageloader.ImageLoader
import java.net.URL

/**
 * [Storage] for [Toot]s.
 *
 * @param profileCache [Cache] of [Profile]s with which [Mastodon toot entities][MastodonTootEntity]
 *   will be converted to [Toot]s.
 * @param tootEntityDao [MastodonStyleEntityDao] that will perform SQL transactions on
 *   [Mastodon toot entities][MastodonTootEntity].
 * @param styleEntityDao [MastodonStyleEntityDao] for inserting and deleting
 *   [Mastodon style entities][MastodonStyleEntity].
 * @param coverLoaderProvider [ImageLoader.Provider] that provides the [ImageLoader] by which a
 *   [Toot]'s [content][Toot.content]'s [highlight][Content.highlight]'s
 *   [headline][Highlight.headline] cover will be loaded from a [URL].
 */
internal class MastodonTootStorage(
  private val profileCache: Cache<Profile>,
  private val tootEntityDao: MastodonTootEntityDao,
  private val styleEntityDao: MastodonStyleEntityDao,
  private val coverLoaderProvider: ImageLoader.Provider<URL>
) : Storage<Toot>() {
  override suspend fun onStore(key: String, value: Toot) {
    val tootEntity = MastodonTootEntity.from(value)
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
    val mentionEntities = styleEntityDao.selectByParentID(key)
    styleEntityDao.delete(mentionEntities)
    tootEntityDao.delete(key)
  }

  override suspend fun onClear() {
    tootEntityDao.deleteAll()
  }
}
