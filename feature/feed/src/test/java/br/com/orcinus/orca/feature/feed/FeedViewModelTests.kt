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

package br.com.orcinus.orca.feature.feed

import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isNotEqualTo
import assertk.assertions.prop
import br.com.orcinus.orca.composite.timeline.stat.details.StatsDetails
import kotlin.test.Test
import kotlinx.coroutines.flow.map
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class FeedViewModelTests {
  @Test
  fun emitsToPostPreviewLoadableFlowWhenFavoritingOrUnfavoritingAPost() {
    runFeedViewModelTest {
      postPreviewFlow
        .map { it.stats }
        .test {
          val wasFavorite = awaitItem().isFavorite
          viewModel.favorite(postID)
          assertThat(awaitItem()).prop(StatsDetails::isFavorite).isNotEqualTo(wasFavorite)
        }
    }
  }

  @Test
  fun emitsToPostPreviewLoadableFlowWhenRepostingOrUnreposting() {
    runFeedViewModelTest {
      postPreviewFlow
        .map { it.stats }
        .test {
          val wasReposted = awaitItem().isReposted
          viewModel.repost(postID)
          assertThat(awaitItem()).prop(StatsDetails::isReposted).isNotEqualTo(wasReposted)
        }
    }
  }
}
