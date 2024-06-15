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
import br.com.orcinus.orca.composite.timeline.stat.details.formatted
import br.com.orcinus.orca.core.sample.feed.profile.post.Posts
import br.com.orcinus.orca.platform.core.withSamples
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
        .isEqualTo(PostPreview.getSample(colors).copy(figure = figure))
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
        post.comment.add(Posts.withSamples.first { it != post })
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
