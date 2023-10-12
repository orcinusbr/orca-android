package com.jeanbarrossilva.orca.feature.feed

import androidx.compose.ui.test.assertIsSelected
import androidx.compose.ui.test.filterToOne
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createEmptyComposeRule
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.performClick
import androidx.test.core.app.launchActivity
import com.jeanbarrossilva.orca.core.feed.profile.Profile
import com.jeanbarrossilva.orca.core.sample.feed.profile.sample
import com.jeanbarrossilva.orca.feature.feed.test.FeedActivity
import com.jeanbarrossilva.orca.feature.feed.test.TestFeedModule
import com.jeanbarrossilva.orca.platform.ui.component.timeline.toot.stat.TOOT_PREVIEW_FAVORITE_STAT_TAG
import com.jeanbarrossilva.orca.platform.ui.test.component.timeline.toot.onTootPreviews
import com.jeanbarrossilva.orca.platform.ui.test.component.timeline.toot.time.Time4JTestRule
import com.jeanbarrossilva.orca.std.injector.test.InjectorTestRule
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

internal class FeedFragmentTests {
  @get:Rule val injectorRule = InjectorTestRule { register<FeedModule>(TestFeedModule) }

  @get:Rule val time4JRule = Time4JTestRule()

  @get:Rule val composeRule = createEmptyComposeRule()

  @Test
  fun favoritesToot() {
    runTest {
      val toot = Profile.sample.getToots(page = 0).first().first()
      if (toot.favorite.isEnabled) {
        toot.favorite.toggle()
      }
    }
    launchActivity<FeedActivity>(FeedActivity.getIntent(Profile.sample.id)).use {
      composeRule
        .onTootPreviews()
        .onFirst()
        .onChildren()
        .filterToOne(hasTestTag(TOOT_PREVIEW_FAVORITE_STAT_TAG))
        .performClick()
        .assertIsSelected()
    }
  }
}
