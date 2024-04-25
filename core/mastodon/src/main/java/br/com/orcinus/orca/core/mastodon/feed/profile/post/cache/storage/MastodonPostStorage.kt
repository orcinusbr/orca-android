/*
 * Copyright © 2023-2024 Orcinus
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

package br.com.orcinus.orca.core.mastodon.feed.profile.post.cache.storage

import br.com.orcinus.orca.core.feed.profile.Profile
import br.com.orcinus.orca.core.feed.profile.post.Post
import br.com.orcinus.orca.core.feed.profile.post.content.Content
import br.com.orcinus.orca.core.feed.profile.post.content.highlight.Highlight
import br.com.orcinus.orca.core.mastodon.feed.profile.cache.storage.style.MastodonStyleEntity
import br.com.orcinus.orca.core.mastodon.feed.profile.cache.storage.style.MastodonStyleEntityDao
import br.com.orcinus.orca.core.mastodon.feed.profile.cache.storage.style.toHttpStyleEntity
import br.com.orcinus.orca.core.mastodon.feed.profile.post.stat.comment.MastodonCommentPaginator
import br.com.orcinus.orca.platform.cache.Cache
import br.com.orcinus.orca.platform.cache.Storage
import br.com.orcinus.orca.std.image.ImageLoader
import br.com.orcinus.orca.std.image.SomeImageLoaderProvider
import java.net.URI

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
 *   [headline][Highlight.headline] cover will be loaded from a [URI].
 * @param commentPaginatorProvider [MastodonCommentPaginator.Provider] by which a
 *   [MastodonCommentPaginator] for paginating through the stored [Post]s' comments will be
 *   provided.
 * @see Post.comment
 */
internal class MastodonPostStorage(
  private val profileCache: Cache<Profile>,
  private val postEntityDao: MastodonPostEntityDao,
  private val styleEntityDao: MastodonStyleEntityDao,
  private val coverLoaderProvider: SomeImageLoaderProvider<URI>,
  private val commentPaginatorProvider: MastodonCommentPaginator.Provider
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
    return postEntityDao
      .selectByID(key)
      .toPost(profileCache, postEntityDao, coverLoaderProvider, commentPaginatorProvider)
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
