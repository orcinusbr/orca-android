/*
 * Copyright © 2024 Orcinus
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

package br.com.orcinus.orca.core.sample.feed.profile.composition

import br.com.orcinus.orca.core.auth.actor.ActorProvider
import br.com.orcinus.orca.core.feed.profile.Profile
import br.com.orcinus.orca.core.feed.profile.account.Account
import br.com.orcinus.orca.core.feed.profile.post.Author
import br.com.orcinus.orca.core.feed.profile.post.OwnedPost
import br.com.orcinus.orca.core.feed.profile.post.Post
import br.com.orcinus.orca.core.feed.profile.post.content.Content
import br.com.orcinus.orca.core.sample.auth.actor.sample
import br.com.orcinus.orca.core.sample.feed.profile.account.sample
import br.com.orcinus.orca.core.sample.feed.profile.post.stat.addable.SampleAddableStat
import br.com.orcinus.orca.core.sample.feed.profile.post.stat.toggleable.SampleToggleableStat
import br.com.orcinus.orca.core.sample.image.SampleImageSource
import br.com.orcinus.orca.core.sample.test.image.NoOpSampleImageLoader
import br.com.orcinus.orca.ext.uri.URIBuilder
import br.com.orcinus.orca.std.markdown.Markdown
import java.time.ZonedDateTime
import java.util.UUID
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

/** [Composer] whose information is hardcoded and don't necessarily represent sample data. */
internal class HardcodedComposer : Composer {
  override val id = UUID.randomUUID().toString()
  override val account = Account.sample
  override val avatarLoader = NoOpSampleImageLoader.Provider.provide(SampleImageSource.None)
  override val name = ""
  override val bio = Markdown.empty
  override val followerCount = 0
  override val followingCount = 0
  override val uri =
    URIBuilder.url()
      .scheme("https")
      .host("orca.orcinus.com.br")
      .path("app")
      .path("profiles")
      .path(id)
      .build()
  override val postsFlow = MutableStateFlow(emptyList<Post>())

  override suspend fun getPosts(page: Int): Flow<List<Post>> {
    return postsFlow.map {
      it.windowed(Composer.MAX_POST_COUNT_PER_PAGE, partialWindows = true).getOrElse(page) {
        emptyList()
      }
    }
  }

  override fun createPost(
    author: Author,
    content: Content,
    publicationDateTime: ZonedDateTime
  ): Post {
    return object : Post() {
      override val actorProvider = ActorProvider.sample
      override val id = UUID.randomUUID().toString()
      override val author = author
      override val content = content
      override val publicationDateTime = publicationDateTime
      override val comment = SampleAddableStat<Post>()
      override val favorite = SampleToggleableStat<Profile>(this@HardcodedComposer)
      override val repost = SampleToggleableStat<Profile>(this@HardcodedComposer)
      override val uri =
        URIBuilder.url()
          .scheme("https")
          .host("orca.orcinus.com.br")
          .path("app")
          .path("posts")
          .path(id)
          .build()

      override suspend fun toOwnedPost(): OwnedPost {
        return let {
          object : OwnedPost(it) {
            override suspend fun remove() {
              postsFlow.value -= it
            }
          }
        }
      }
    }
  }
}
