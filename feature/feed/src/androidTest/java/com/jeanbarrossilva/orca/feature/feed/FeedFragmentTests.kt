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
import androidx.compose.ui.test.performScrollToNode
import androidx.test.core.app.launchActivity
import com.jeanbarrossilva.orca.core.auth.actor.Actor
import com.jeanbarrossilva.orca.core.feed.profile.Profile
import com.jeanbarrossilva.orca.core.feed.profile.toot.Toot
import com.jeanbarrossilva.orca.core.instance.Instance
import com.jeanbarrossilva.orca.core.sample.auth.actor.sample
import com.jeanbarrossilva.orca.core.sample.test.feed.profile.sample
import com.jeanbarrossilva.orca.core.sample.test.feed.profile.toot.samples
import com.jeanbarrossilva.orca.core.sample.test.instance.SampleInstanceTestRule
import com.jeanbarrossilva.orca.core.sample.test.instance.sample
import com.jeanbarrossilva.orca.feature.feed.test.FeedActivity
import com.jeanbarrossilva.orca.feature.feed.test.TestFeedModule
import com.jeanbarrossilva.orca.platform.ui.component.timeline.toot.stat.TOOT_PREVIEW_FAVORITE_STAT_TAG
import com.jeanbarrossilva.orca.platform.ui.test.component.timeline.isRenderEffect
import com.jeanbarrossilva.orca.platform.ui.test.component.timeline.onTimeline
import com.jeanbarrossilva.orca.platform.ui.test.component.timeline.toot.isTootPreview
import com.jeanbarrossilva.orca.platform.ui.test.component.timeline.toot.onTootPreviews
import com.jeanbarrossilva.orca.platform.ui.test.component.timeline.toot.time.Time4JTestRule
import com.jeanbarrossilva.orca.std.injector.test.InjectorTestRule
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

internal class FeedFragmentTests {
  @get:Rule val injectorRule = InjectorTestRule { register<FeedModule>(TestFeedModule) }
  @get:Rule val sampleInstanceRule = SampleInstanceTestRule()
  @get:Rule val time4JRule = Time4JTestRule()
  @get:Rule val composeRule = createEmptyComposeRule()

  @Test
  fun keepsPreviousTootsWhenLoadingNextOnes() {
    launchActivity<FeedActivity>(FeedActivity.getIntent(Profile.sample.id)).use {
      composeRule
        .onTimeline()
        .performScrollToNode(isRenderEffect())
        .onChildren()
        .filter(isTootPreview())
        .assertCountEquals(Toot.samples.size)
    }
  }

  @Test
  fun favoritesToot() {
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
        .onTootPreviews()
        .onFirst()
        .onChildren()
        .filterToOne(hasTestTag(TOOT_PREVIEW_FAVORITE_STAT_TAG))
        .performScrollTo()
        .performClick()
        .assertIsSelected()
    }
  }
}
