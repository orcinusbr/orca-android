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

package br.com.orcinus.orca.composite.timeline.stat.details

import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isTrue
import br.com.orcinus.orca.core.sample.feed.profile.post.Posts
import br.com.orcinus.orca.platform.core.withSample
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class PostExtensionsTests {
  @Test
  fun favoriteCountDependsOnEnableabilityWhenConvertingPostIntoStatDetailsFlow() {
    val post = Posts.withSample.single()
    val previousFavoriteCount = post.favorite.count
    runTest {
      post.asStatsDetailsFlow().test {
        awaitItem()
        post.favorite.enable()
        awaitItem().let {
          assertThat(it.isFavorite).isTrue()
          assertThat(it.formattedFavoriteCount).isEqualTo(previousFavoriteCount.inc().formatted)
        }
      }
    }
  }

  @Test
  fun repostCountDependsOnEnableabilityWhenConvertingPostIntoStatDetailsFlow() {
    val post = Posts.withSample.single()
    val previousRepostCount = post.repost.count
    runTest {
      post.asStatsDetailsFlow().test {
        awaitItem()
        post.repost.enable()
        awaitItem().let {
          assertThat(it.isReposted).isTrue()
          assertThat(it.formattedReblogCount).isEqualTo(previousRepostCount.inc().formatted)
        }
      }
    }
  }
}
