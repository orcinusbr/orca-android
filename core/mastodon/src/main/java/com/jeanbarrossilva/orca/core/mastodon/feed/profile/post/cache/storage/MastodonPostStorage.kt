package com.jeanbarrossilva.orca.core.mastodon.feed.profile.post.cache.storage

import com.jeanbarrossilva.orca.core.feed.profile.Profile
import com.jeanbarrossilva.orca.core.feed.profile.post.Post
import com.jeanbarrossilva.orca.core.feed.profile.post.content.Content
import com.jeanbarrossilva.orca.core.feed.profile.post.content.highlight.Highlight
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.cache.storage.style.MastodonStyleEntity
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.cache.storage.style.MastodonStyleEntityDao
import com.jeanbarrossilva.orca.core.mastodon.feed.profile.cache.storage.style.toHttpStyleEntity
import com.jeanbarrossilva.orca.platform.cache.Cache
import com.jeanbarrossilva.orca.platform.cache.Storage
import com.jeanbarrossilva.orca.std.imageloader.ImageLoader
import java.net.URL

/**
 * [Storage] for [Post]s.
 *
 * @param profileCache [Cache] of [Profile]s with which [Mastodon post entities][MastodonPostEntity]
 *   will be converted to [Post]s.
 * @param postEntityDao [MastodonStyleEntityDao] that will perform SQL transactions on
 *   [Mastodon post entities][MastodonPostEntity].
 * @param styleEntityDao [MastodonStyleEntityDao] for inserting and deleting
 *   [Mastodon style entities][MastodonStyleEntity].
 * @param coverLoaderProvider [ImageLoader.Provider] that provides the [ImageLoader] by which a
 *   [Post]'s [content][Post.content]'s [highlight][Content.highlight]'s
 *   [headline][Highlight.headline] cover will be loaded from a [URL].
 */
internal class MastodonPostStorage(
  private val profileCache: Cache<Profile>,
  private val postEntityDao: MastodonPostEntityDao,
  private val styleEntityDao: MastodonStyleEntityDao,
  private val coverLoaderProvider: ImageLoader.Provider<URL>
) : Storage<Post>() {
  override suspend fun onStore(key: String, value: Post) {
    val postEntity = MastodonPostEntity.from(value)
    val styleEntities = value.content.text.styles.map { it.toHttpStyleEntity(value.id) }
    postEntityDao.insert(postEntity)
    styleEntityDao.insert(styleEntities)
  }

  override suspend fun onContains(key: String): Boolean {
    return postEntityDao.count(key) > 0
  }

  override suspend fun onGet(key: String): Post {
    return postEntityDao.selectByID(key).toPost(profileCache, postEntityDao, coverLoaderProvider)
  }

  override suspend fun onRemove(key: String) {
    val mentionEntities = styleEntityDao.selectByParentID(key)
    styleEntityDao.delete(mentionEntities)
    postEntityDao.delete(key)
  }

  override suspend fun onClear() {
    postEntityDao.deleteAll()
  }
}
