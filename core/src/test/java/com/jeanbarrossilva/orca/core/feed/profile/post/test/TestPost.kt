/*
 * Copyright © 2023 Orca
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see https://www.gnu.org/licenses.
 */

package com.jeanbarrossilva.orca.core.feed.profile.post.test

import com.jeanbarrossilva.orca.core.feed.profile.Profile
import com.jeanbarrossilva.orca.core.feed.profile.post.Author
import com.jeanbarrossilva.orca.core.feed.profile.post.DeletablePost
import com.jeanbarrossilva.orca.core.feed.profile.post.Post
import com.jeanbarrossilva.orca.core.feed.profile.post.content.Content
import com.jeanbarrossilva.orca.core.feed.profile.post.stat.Stat
import com.jeanbarrossilva.orca.core.feed.profile.post.stat.toggleable.ToggleableStat
import com.jeanbarrossilva.orca.core.sample.test.feed.profile.post.sample
import java.net.URL
import java.time.ZonedDateTime

/** Local [Post] that defaults its properties' values to [Post.Companion.sample]'s. */
internal class TestPost(
  override val id: String = Post.sample.id,
  override val author: Author = Post.sample.author,
  override val content: Content = Post.sample.content,
  override val publicationDateTime: ZonedDateTime = Post.sample.publicationDateTime,
  override val comment: Stat<Post> = Post.sample.comment,
  override val favorite: ToggleableStat<Profile> = Post.sample.favorite,
  override val repost: ToggleableStat<Profile> = Post.sample.repost,
  override val url: URL = Post.sample.url
) : Post() {
  override fun asDeletable(): DeletablePost {
    return object : DeletablePost(this@TestPost) {
      override suspend fun delete() {}
    }
  }
}
