package com.jeanbarrossilva.orca.feature.feed

import androidx.compose.ui.test.filterToOne
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onFirst
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import com.jeanbarrossilva.loadable.list.toListLoadable
import com.jeanbarrossilva.loadable.list.toSerializableList
import com.jeanbarrossilva.orca.platform.theme.OrcaTheme
import com.jeanbarrossilva.orca.platform.ui.component.timeline.toot.TootPreview
import com.jeanbarrossilva.orca.platform.ui.component.timeline.toot.stat.TOOT_PREVIEW_FAVORITE_STAT_TAG
import com.jeanbarrossilva.orca.platform.ui.test.component.timeline.toot.onTootPreviews
import com.jeanbarrossilva.orca.platform.ui.test.component.timeline.toot.time.Time4JTestRule
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

internal class FeedTests {
  @get:Rule val time4JRule = Time4JTestRule()

  @get:Rule val composeRule = createComposeRule()

  @Test
  fun runsCallbackWhenClickingTootFavoriteStat() {
    var hasCallbackBeenRun = false
    composeRule.setContent {
      OrcaTheme {
        Feed(
          TootPreview.samples.toSerializableList().toListLoadable(),
          onFavorite = { hasCallbackBeenRun = true }
        )
      }
    }
    composeRule
      .onTootPreviews()
      .onFirst()
      .onChildren()
      .filterToOne(hasTestTag(TOOT_PREVIEW_FAVORITE_STAT_TAG))
      .performScrollTo()
      .performClick()
    assertTrue(hasCallbackBeenRun)
  }
}
