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

package br.com.orcinus.orca.composite.timeline.post

import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotEqualTo
import br.com.orcinus.orca.composite.timeline.avatar.sample
import br.com.orcinus.orca.composite.timeline.post.conversion.runPostToPostPreviewConversionTest
import br.com.orcinus.orca.composite.timeline.stat.details.formatted
import br.com.orcinus.orca.core.auth.actor.ActorProvider
import br.com.orcinus.orca.core.feed.profile.Profile
import br.com.orcinus.orca.core.feed.profile.post.Author
import br.com.orcinus.orca.core.feed.profile.post.OwnedPost
import br.com.orcinus.orca.core.feed.profile.post.Post
import br.com.orcinus.orca.core.feed.profile.post.content.Content
import br.com.orcinus.orca.core.feed.profile.post.stat.addable.AddableStat
import br.com.orcinus.orca.core.feed.profile.post.stat.toggleable.ToggleableStat
import br.com.orcinus.orca.core.instance.domain.Domain
import br.com.orcinus.orca.core.sample.auth.actor.sample
import br.com.orcinus.orca.core.sample.instance.domain.sample
import br.com.orcinus.orca.ext.uri.url.HostedURLBuilder
import br.com.orcinus.orca.std.markdown.Markdown
import java.time.ZonedDateTime
import java.util.UUID
import kotlin.test.Test
import kotlinx.coroutines.flow.drop
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class PostExtensionsTests {
  @Test
  fun convertsIntoPostPreview() {
    runPostToPostPreviewConversionTest {
      assertThat(post.toPostPreview(colors, onLinkClick, onThumbnailClickListener))
        .isEqualTo(PostPreview.createSample(postProvider, colors).copy(figure = figure))
    }
  }

  @Test
  fun convertsIntoPostPreviewFlow() {
    runPostToPostPreviewConversionTest {
      post.toPostPreviewFlow(colors, onLinkClick, onThumbnailClickListener).test {
        assertThat(awaitItem())
          .isEqualTo(post.toPostPreview(colors, onLinkClick, onThumbnailClickListener))
      }
    }
  }

  @Test
  fun emitsAnotherPostPreviewToFlowWhenCommentingOnThePost() {
    runPostToPostPreviewConversionTest {
      post.toPostPreviewFlow(colors, onLinkClick, onThumbnailClickListener).drop(1).test {
        val previousCount = post.comment.count
        post.comment.add(
          object : Post() {
            override val actorProvider = ActorProvider.sample
            override val id = UUID.randomUUID().toString()
            override val author = Author.sample
            override val content = Content.from(Domain.sample, text = Markdown.empty) { null }
            override val publicationDateTime = ZonedDateTime.now()
            override val comment = AddableStat<Post>()
            override val favorite = ToggleableStat<Profile>()
            override val repost = ToggleableStat<Profile>()
            override val uri =
              HostedURLBuilder.from(Domain.sample.uri)
                .path("${author.account.username}")
                .path("posts")
                .path(id)
                .build()

            @Throws(UnsupportedOperationException::class)
            override suspend fun toOwnedPost(): OwnedPost {
              throw UnsupportedOperationException()
            }
          }
        )
        assertThat(awaitItem().stats.formattedCommentCount).isEqualTo(previousCount.inc().formatted)
      }
    }
  }

  @Test
  fun emitsAnotherPostPreviewToFlowWhenFavoritingOrUnfavoritingThePost() {
    runPostToPostPreviewConversionTest {
      post.toPostPreviewFlow(colors, onLinkClick, onThumbnailClickListener).drop(1).test {
        val previousCount = post.favorite.count
        val wasFavorite = post.favorite.isEnabled
        post.favorite.toggle()
        awaitItem().let {
          assertThat(it.stats.isFavorite).isNotEqualTo(wasFavorite)
          assertThat(it.stats.formattedFavoriteCount)
            .isEqualTo((previousCount + if (wasFavorite) -1 else 1).formatted)
        }
      }
    }
  }

  @Test
  fun emitsAnotherPostPreviewToFlowWhenRepostingOrUnreposting() {
    runPostToPostPreviewConversionTest {
      post.toPostPreviewFlow(colors, onLinkClick, onThumbnailClickListener).drop(1).test {
        val previousCount = post.repost.count
        val wasReposted = post.repost.isEnabled
        post.repost.toggle()
        awaitItem().let {
          assertThat(it.stats.isReposted).isNotEqualTo(wasReposted)
          assertThat(it.stats.formattedReblogCount)
            .isEqualTo((previousCount + if (wasReposted) -1 else 1).formatted)
        }
      }
    }
  }
}
