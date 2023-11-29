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
import com.jeanbarrossilva.orca.platform.autos.theme.AutosTheme
import com.jeanbarrossilva.orca.platform.ui.component.timeline.post.PostPreview
import com.jeanbarrossilva.orca.platform.ui.component.timeline.post.stat.POST_PREVIEW_FAVORITE_STAT_TAG
import com.jeanbarrossilva.orca.platform.ui.test.component.timeline.post.onPostPreviews
import com.jeanbarrossilva.orca.platform.ui.test.component.timeline.post.time.Time4JTestRule
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

internal class FeedTests {
  @get:Rule val time4JRule = Time4JTestRule()

  @get:Rule val composeRule = createComposeRule()

  @Test
  fun runsCallbackWhenClickingPostFavoriteStat() {
    var hasCallbackBeenRun = false
    composeRule.setContent {
      AutosTheme {
        Feed(
          PostPreview.samples.toSerializableList().toListLoadable(),
          onFavorite = { hasCallbackBeenRun = true }
        )
      }
    }
    composeRule
      .onPostPreviews()
      .onFirst()
      .onChildren()
      .filterToOne(hasTestTag(POST_PREVIEW_FAVORITE_STAT_TAG))
      .performScrollTo()
      .performClick()
    assertTrue(hasCallbackBeenRun)
  }
}
