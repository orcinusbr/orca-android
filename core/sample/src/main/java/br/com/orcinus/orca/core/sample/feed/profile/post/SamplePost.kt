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

package br.com.orcinus.orca.core.sample.feed.profile.post

import br.com.orcinus.orca.core.feed.profile.Profile
import br.com.orcinus.orca.core.feed.profile.post.Author
import br.com.orcinus.orca.core.feed.profile.post.DeletablePost
import br.com.orcinus.orca.core.feed.profile.post.Post
import br.com.orcinus.orca.core.feed.profile.post.content.Content
import br.com.orcinus.orca.core.feed.profile.post.stat.toggleable.ToggleableStat
import br.com.orcinus.orca.core.sample.feed.profile.post.stat.createSampleAddableStat
import java.net.URI
import java.time.ZonedDateTime

/**
 * [Post] whose operations are performed in memory and serves as a sample.
 *
 * @param writerProvider [SamplePostWriter.Provider] by which a [SamplePostWriter] for creating a
 *   [SampleDeletablePost] from this [SamplePost] can be provided.
 */
internal data class SamplePost(
  override val id: String,
  override val author: Author,
  override val content: Content,
  override val publicationDateTime: ZonedDateTime,
  override val favorite: ToggleableStat<Profile>,
  override val repost: ToggleableStat<Profile>,
  override val uri: URI,
  val writerProvider: SamplePostWriter.Provider
) : Post {
  override val comment = createSampleAddableStat<Post>()

  override fun asDeletable(): DeletablePost {
    return SampleDeletablePost(this)
  }
}
