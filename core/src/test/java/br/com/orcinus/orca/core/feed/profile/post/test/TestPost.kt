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

package br.com.orcinus.orca.core.feed.profile.post.test

import br.com.orcinus.orca.core.feed.profile.Profile
import br.com.orcinus.orca.core.feed.profile.post.Author
import br.com.orcinus.orca.core.feed.profile.post.DeletablePost
import br.com.orcinus.orca.core.feed.profile.post.Post
import br.com.orcinus.orca.core.feed.profile.post.content.Content
import br.com.orcinus.orca.core.feed.profile.post.stat.addable.AddableStat
import br.com.orcinus.orca.core.feed.profile.post.stat.toggleable.ToggleableStat
import br.com.orcinus.orca.core.sample.feed.profile.post.Posts
import br.com.orcinus.orca.core.sample.test.feed.profile.post.withSample
import java.net.URI
import java.time.ZonedDateTime

/** Local [Post] that defaults its properties' values to [Posts.Companion.withSample]'s sample. */
internal class TestPost(
  override val id: String = delegate.id,
  override val author: Author = delegate.author,
  override val content: Content = delegate.content,
  override val publicationDateTime: ZonedDateTime = delegate.publicationDateTime,
  override val comment: AddableStat<Post> = delegate.comment,
  override val favorite: ToggleableStat<Profile> = delegate.favorite,
  override val repost: ToggleableStat<Profile> = delegate.repost,
  override val uri: URI = delegate.uri
) : Post {
  override fun asDeletable(): DeletablePost {
    return delegate.asDeletable()
  }

  companion object {
    /** [Post] to which a [TestPost]'s functionality is delegated. */
    private val delegate = Posts.withSample.single()
  }
}
