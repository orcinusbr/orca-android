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

package br.com.orcinus.orca.core.sample.feed.profile.post

import br.com.orcinus.orca.core.auth.actor.ActorProvider
import br.com.orcinus.orca.core.feed.profile.Profile
import br.com.orcinus.orca.core.feed.profile.post.Author
import br.com.orcinus.orca.core.feed.profile.post.OwnedPost
import br.com.orcinus.orca.core.feed.profile.post.Post
import br.com.orcinus.orca.core.feed.profile.post.content.Content
import br.com.orcinus.orca.core.instance.domain.Domain
import br.com.orcinus.orca.core.sample.auth.actor.sample
import br.com.orcinus.orca.core.sample.feed.profile.post.stat.addable.SampleAddableStat
import br.com.orcinus.orca.core.sample.feed.profile.post.stat.toggleable.SampleToggleableStat
import br.com.orcinus.orca.core.sample.instance.domain.sample
import br.com.orcinus.orca.ext.uri.url.HostedURLBuilder
import java.time.ZonedDateTime
import java.util.UUID

/**
 * [Post] whose operations are performed in memory and serves as a sample.
 *
 * @property provider [SamplePostProvider] by which a [SampleOwnedPost] into which this [SamplePost]
 *   can be converted is removed.
 * @property owner [Profile] in which this [SamplePost] is.
 * @see SamplePost.own
 * @see SampleOwnedPost.remove
 */
internal data class SamplePost(
  internal val provider: SamplePostProvider,
  private val owner: Profile,
  override val content: Content,
  override val publicationDateTime: ZonedDateTime
) : Post() {
  override val actorProvider = ActorProvider.sample
  override val id = UUID.randomUUID().toString()
  override val author = Author(owner.id, owner.avatarLoader, owner.name, owner.account, owner.uri)
  override val comment = SampleAddableStat<Post>()
  override val favorite = SampleToggleableStat(owner)
  override val repost = SampleToggleableStat(owner)
  override val uri =
    HostedURLBuilder.from(Domain.sample.uri)
      .path("${owner.account.username}")
      .path("posts")
      .path(id)
      .build()

  override fun toString(): String {
    return "Post $id by ${author.account}"
  }

  override suspend fun toOwnedPost(): OwnedPost {
    return SampleOwnedPost(provider, this)
  }
}
