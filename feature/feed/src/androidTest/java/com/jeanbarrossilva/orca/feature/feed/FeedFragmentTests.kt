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

package com.jeanbarrossilva.orca.feature.feed

import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.filter
import androidx.compose.ui.test.filterToOne
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createEmptyComposeRule
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.test.core.app.launchActivity
import com.jeanbarrossilva.orca.core.auth.actor.Actor
import com.jeanbarrossilva.orca.core.feed.profile.Profile
import com.jeanbarrossilva.orca.core.instance.Instance
import com.jeanbarrossilva.orca.core.sample.auth.actor.sample
import com.jeanbarrossilva.orca.core.sample.feed.profile.post.Posts
import com.jeanbarrossilva.orca.core.sample.test.feed.profile.post.withSamples
import com.jeanbarrossilva.orca.core.sample.test.instance.SampleInstanceTestRule
import com.jeanbarrossilva.orca.feature.feed.test.FeedActivity
import com.jeanbarrossilva.orca.feature.feed.test.TestFeedModule
import com.jeanbarrossilva.orca.platform.core.sample
import com.jeanbarrossilva.orca.platform.ui.component.stat.favorite.FAVORITE_STAT_TAG
import com.jeanbarrossilva.orca.platform.ui.test.component.timeline.onTimeline
import com.jeanbarrossilva.orca.platform.ui.test.component.timeline.performScrollToBottom
import com.jeanbarrossilva.orca.platform.ui.test.component.timeline.post.isPostPreview
import com.jeanbarrossilva.orca.platform.ui.test.component.timeline.post.onPostPreviews
import com.jeanbarrossilva.orca.platform.ui.test.component.timeline.post.time.Time4JTestRule
import com.jeanbarrossilva.orca.std.injector.test.InjectorTestRule
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

internal class FeedFragmentTests {
  @get:Rule val injectorRule = InjectorTestRule { register<FeedModule>(TestFeedModule) }
  @get:Rule val sampleInstanceRule = SampleInstanceTestRule(Instance.sample)
  @get:Rule val time4JRule = Time4JTestRule()
  @get:Rule val composeRule = createEmptyComposeRule()

  @Test
  fun keepsPreviousPostsWhenLoadingNextOnes() {
    launchActivity<FeedActivity>(FeedActivity.getIntent(Profile.sample.id)).use {
      composeRule
        .onTimeline()
        .performScrollToBottom()
        .onChildren()
        .filter(isPostPreview())
        .assertCountEquals(Posts.withSamples.size)
    }
  }

  @Test
  fun favoritesPost() {
    runTest {
      Instance.sample.feedProvider
        .provide(Actor.Authenticated.sample.id, page = 0)
        .first()
        .first()
        .favorite
        .disable()
    }
    launchActivity<FeedActivity>(FeedActivity.getIntent(Profile.sample.id)).use {
      composeRule
        .onPostPreviews()
        .onFirst()
        .onChildren()
        .filterToOne(hasTestTag(FAVORITE_STAT_TAG))
        .performScrollTo()
        .performClick()
        .assertIsSelected()
    }
  }
}
