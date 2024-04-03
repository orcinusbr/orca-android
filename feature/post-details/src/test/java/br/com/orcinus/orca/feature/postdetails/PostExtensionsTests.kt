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

package br.com.orcinus.orca.feature.postdetails

import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isEqualTo
import br.com.orcinus.orca.composite.timeline.post.figure.Figure
import br.com.orcinus.orca.composite.timeline.stat.details.asStatsDetails
import br.com.orcinus.orca.composite.timeline.text.annotated.toAnnotatedString
import br.com.orcinus.orca.core.instance.Instance
import br.com.orcinus.orca.core.sample.test.instance.SampleInstanceTestRule
import br.com.orcinus.orca.feature.postdetails.conversion.runPostDetailsConversionTest
import br.com.orcinus.orca.platform.core.sample
import br.com.orcinus.orca.std.image.compose.SomeComposableImageLoader
import kotlin.test.Test
import org.junit.Rule
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class PostExtensionsTests {
  @get:Rule val sampleInstanceRule = SampleInstanceTestRule(Instance.sample)

  @Test
  fun convertsPostIntoDetails() {
    runPostDetailsConversionTest {
      assertThat(post().toPostDetails(colors, onLinkClick, onThumbnailClickListener))
        .isEqualTo(
          PostDetails(
            post().id,
            post().author.avatarLoader as SomeComposableImageLoader,
            post().author.name,
            post().author.account,
            post().content.text.toAnnotatedString(colors),
            Figure.of(
              post().id,
              post().author.name,
              post().content,
              onLinkClick,
              onThumbnailClickListener
            ),
            post().publicationDateTime,
            post().asStatsDetails(),
            post().url
          )
        )
    }
  }

  @Test
  fun emitsWhenConvertingPostIntoFlowOfDetailsAfterCommentIsAdded() {
    runPostDetailsConversionTest {
      post().comment.count.let { previousCommentCount ->
        post().toPostDetailsFlow(colors, onLinkClick, onThumbnailClickListener).test {
          awaitItem()
          comment()
          assertThat(awaitItem())
            .isEqualTo(detailed().withCommentCountOf(previousCommentCount.inc()))
        }
      }
    }
  }

  @Test
  fun emitsWhenConvertingPostIntoFlowOfDetailsAfterCommentIsRemoved() {
    runPostDetailsConversionTest {
      comment()
      post().comment.count.let { previousCommentCount ->
        post().toPostDetailsFlow(colors, onLinkClick, onThumbnailClickListener).test {
          awaitItem()
          uncomment()
          assertThat(awaitItem())
            .isEqualTo(detailed().withCommentCountOf(previousCommentCount.dec()))
        }
      }
    }
  }

  @Test
  fun emitsWhenConvertingPostIntoFlowOfDetailsAfterFavoriting() {
    runPostDetailsConversionTest {
      post().toPostDetailsFlow(colors, onLinkClick, onThumbnailClickListener).test {
        awaitItem()
        favorite()
        assertThat(awaitItem()).isEqualTo(detailed().favorited())
      }
    }
  }

  @Test
  fun emitsWhenConvertingPostIntoFlowOfDetailsAfterUnfavoriting() {
    runPostDetailsConversionTest {
      favorite()
      post().toPostDetailsFlow(colors, onLinkClick, onThumbnailClickListener).test {
        awaitItem()
        unfavorite()
        assertThat(awaitItem()).isEqualTo(detailed().unfavorited())
      }
    }
  }

  @Test
  fun emitsWhenConvertingPostIntoFlowOfDetailsAfterReposting() {
    runPostDetailsConversionTest {
      post().toPostDetailsFlow(colors, onLinkClick, onThumbnailClickListener).test {
        awaitItem()
        repost()
        assertThat(awaitItem()).isEqualTo(detailed().reposted())
      }
    }
  }

  @Test
  fun emitsWhenConvertingPostIntoFlowOfDetailsAfterUnreposting() {
    runPostDetailsConversionTest {
      repost()
      post().toPostDetailsFlow(colors, onLinkClick, onThumbnailClickListener).test {
        awaitItem()
        unrepost()
        assertThat(awaitItem()).isEqualTo(detailed().unreposted())
      }
    }
  }
}
