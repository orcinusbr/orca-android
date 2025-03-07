/*
 * Copyright © 2023–2025 Orcinus
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

package br.com.orcinus.orca.core.feed.profile.account.reblog

import br.com.orcinus.orca.core.auth.actor.ActorProvider
import br.com.orcinus.orca.core.feed.profile.post.OwnedPost
import br.com.orcinus.orca.core.feed.profile.post.repost.Repost
import br.com.orcinus.orca.core.sample.auth.actor.sample
import br.com.orcinus.orca.core.sample.instance.SampleInstance
import br.com.orcinus.orca.core.sample.test.image.NoOpSampleImageLoader
import br.com.orcinus.orca.std.func.monad.Maybe
import kotlin.test.Test
import kotlin.test.assertEquals

internal class RepostTests {
  private val sampleRepost =
    SampleInstance.Builder.create(NoOpSampleImageLoader.Provider)
      .withDefaultProfiles()
      .withDefaultPosts()
      .build()
      .postProvider
      .provideAllCurrent()
      .filterIsInstance<Repost>()
      .first()

  @Test
  fun createsRepost() {
    assertEquals(
      object : Repost() {
        override val actorProvider = ActorProvider.sample
        override val id = sampleRepost.id
        override val author = sampleRepost.author
        override val reposter = sampleRepost.reposter
        override val content = sampleRepost.content
        override val publicationDateTime = sampleRepost.publicationDateTime
        override val comment = sampleRepost.comment
        override val favorite = sampleRepost.favorite
        override val repost = sampleRepost.repost
        override val uri = sampleRepost.uri

        override suspend fun toOwnedPost(): OwnedPost {
          return let {
            object : OwnedPost(it) {
              override suspend fun remove() = Maybe.successful()
            }
          }
        }
      },
      Repost(
        sampleRepost.getActorProvider(),
        sampleRepost.id,
        sampleRepost.author,
        sampleRepost.reposter,
        sampleRepost.content,
        sampleRepost.publicationDateTime,
        sampleRepost.comment,
        sampleRepost.favorite,
        sampleRepost.repost,
        sampleRepost.uri
      ) {
        object : OwnedPost(it) {
          override suspend fun remove() = Maybe.successful()
        }
      }
    )
  }

  @Test
  fun createsRepostFromOriginalPost() {
    assertEquals(sampleRepost, Repost(sampleRepost, sampleRepost.reposter))
  }
}
