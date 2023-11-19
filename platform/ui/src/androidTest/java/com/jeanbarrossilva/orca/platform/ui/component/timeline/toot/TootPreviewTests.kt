package com.jeanbarrossilva.orca.platform.ui.component.timeline.toot

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onLast
import androidx.test.platform.app.InstrumentationRegistry
import com.jeanbarrossilva.loadable.placeholder.test.assertIsLoading
import com.jeanbarrossilva.loadable.placeholder.test.assertIsNotLoading
import com.jeanbarrossilva.orca.core.feed.profile.toot.Author
import com.jeanbarrossilva.orca.core.sample.test.feed.profile.toot.sample
import com.jeanbarrossilva.orca.core.sample.test.instance.SampleInstanceTestRule
import com.jeanbarrossilva.orca.platform.theme.OrcaTheme
import com.jeanbarrossilva.orca.platform.ui.R
import com.jeanbarrossilva.orca.platform.ui.component.timeline.toot.test.onStatLabel
import com.jeanbarrossilva.orca.platform.ui.component.timeline.toot.test.onTootPreviewBody
import com.jeanbarrossilva.orca.platform.ui.component.timeline.toot.test.onTootPreviewCommentCountStat
import com.jeanbarrossilva.orca.platform.ui.component.timeline.toot.test.onTootPreviewFavoriteCountStat
import com.jeanbarrossilva.orca.platform.ui.component.timeline.toot.test.onTootPreviewMetadata
import com.jeanbarrossilva.orca.platform.ui.component.timeline.toot.test.onTootPreviewName
import com.jeanbarrossilva.orca.platform.ui.component.timeline.toot.test.onTootPreviewReblogCountStat
import com.jeanbarrossilva.orca.platform.ui.component.timeline.toot.test.onTootPreviewReblogMetadata
import com.jeanbarrossilva.orca.platform.ui.component.timeline.toot.test.onTootPreviewShareAction
import com.jeanbarrossilva.orca.platform.ui.test.component.timeline.toot.onTootPreview
import com.jeanbarrossilva.orca.platform.ui.test.component.timeline.toot.time.TestRelativeTimeProvider
import org.junit.Rule
import org.junit.Test

internal class TootPreviewTests {
  @get:Rule val coreSampleRule = SampleInstanceTestRule()

  @get:Rule val composeRule = createComposeRule()

  private val sampleTootPreview
    get() = TootPreview.getSample(context, OrcaTheme.getColors(context))

  private val context
    get() = InstrumentationRegistry.getInstrumentation().context

  @Test
  fun isShownWhenLoading() {
    composeRule.setContent { OrcaTheme { TootPreview() } }
    composeRule.onTootPreview().assertIsDisplayed()
  }

  @Test
  fun isShownWhenLoaded() {
    composeRule.setContent {
      OrcaTheme { SampleTootPreview(relativeTimeProvider = TestRelativeTimeProvider) }
    }
    composeRule.onTootPreview().assertIsDisplayed()
  }

  @Test
  fun nameIsLoadingWhenLoading() {
    composeRule.setContent { OrcaTheme { TootPreview() } }
    composeRule.onTootPreviewName().assertIsLoading()
  }

  @Test
  fun nameIsShownWhenLoaded() {
    composeRule.setContent {
      OrcaTheme { SampleTootPreview(relativeTimeProvider = TestRelativeTimeProvider) }
    }
    composeRule.onTootPreviewName().assertIsNotLoading()
    composeRule.onTootPreviewName().assertTextEquals(sampleTootPreview.name)
  }

  @Test
  fun metadataIsLoadingWhenLoading() {
    composeRule.setContent { OrcaTheme { TootPreview() } }
    composeRule.onTootPreviewMetadata().assertIsLoading()
  }

  @Test
  fun metadataIsShownWhenLoaded() {
    val relativeTimeProvider = TestRelativeTimeProvider
    composeRule.setContent {
      OrcaTheme { SampleTootPreview(relativeTimeProvider = relativeTimeProvider) }
    }
    composeRule.onTootPreviewMetadata().assertIsNotLoading()
    composeRule
      .onTootPreviewMetadata()
      .assertTextEquals(sampleTootPreview.getMetadata(relativeTimeProvider))
  }

  @Test
  fun reblogMetadataIsNotShownWhenLoading() {
    composeRule.setContent { OrcaTheme { TootPreview() } }
    composeRule.onTootPreviewReblogMetadata().assertDoesNotExist()
  }

  @Test
  fun reblogMetadataIndicatesThatTootHasBeenRebloggedBySomeone() {
    composeRule.setContent {
      OrcaTheme {
        SampleTootPreview(
          preview = TootPreview.sample.copy(rebloggerName = Author.sample.name),
          relativeTimeProvider = TestRelativeTimeProvider
        )
      }
    }
    composeRule
      .onTootPreviewReblogMetadata()
      .onChildren()
      .onLast()
      .assertTextEquals(
        context.getString(R.string.platform_ui_toot_preview_reblogged, Author.sample.name)
      )
  }

  @Test
  fun bodyIsLoadingWhenLoading() {
    composeRule.setContent { OrcaTheme { TootPreview() } }
    composeRule.onTootPreviewBody().assertIsLoading()
  }

  @Test
  fun bodyIsShownWhenLoaded() {
    composeRule.setContent {
      OrcaTheme { SampleTootPreview(relativeTimeProvider = TestRelativeTimeProvider) }
    }
    composeRule.onTootPreviewBody().assertIsNotLoading()
    composeRule.onTootPreviewBody().assertTextEquals(sampleTootPreview.text.text)
  }

  @Test
  fun commentCountStatIsNonexistentWhenLoading() {
    composeRule.setContent { OrcaTheme { TootPreview() } }
    composeRule.onTootPreviewCommentCountStat().assertDoesNotExist()
  }

  @Test
  fun commentCountStatIsShownWhenLoaded() {
    composeRule.setContent {
      OrcaTheme { SampleTootPreview(relativeTimeProvider = TestRelativeTimeProvider) }
    }
    composeRule.onTootPreviewCommentCountStat().assertIsDisplayed()
    composeRule
      .onTootPreviewCommentCountStat()
      .onStatLabel()
      .assertTextEquals(sampleTootPreview.formattedCommentCount)
  }

  @Test
  fun favoriteCountStatIsNonexistentWhenLoading() {
    composeRule.setContent { OrcaTheme { TootPreview() } }
    composeRule.onTootPreviewFavoriteCountStat().assertDoesNotExist()
  }

  @Test
  fun favoriteCountStatIsShownWhenLoaded() {
    composeRule.setContent {
      OrcaTheme { SampleTootPreview(relativeTimeProvider = TestRelativeTimeProvider) }
    }
    composeRule.onTootPreviewFavoriteCountStat().assertIsDisplayed()
    composeRule
      .onTootPreviewFavoriteCountStat()
      .onStatLabel()
      .assertTextEquals(sampleTootPreview.formattedFavoriteCount)
  }

  @Test
  fun reblogCountStatIsNonexistentWhenLoading() {
    composeRule.setContent { OrcaTheme { TootPreview() } }
    composeRule.onTootPreviewReblogCountStat().assertDoesNotExist()
  }

  @Test
  fun reblogCountStatIsShownWhenLoaded() {
    composeRule.setContent {
      OrcaTheme { SampleTootPreview(relativeTimeProvider = TestRelativeTimeProvider) }
    }
    composeRule.onTootPreviewReblogCountStat().assertIsDisplayed()
    composeRule
      .onTootPreviewReblogCountStat()
      .onStatLabel()
      .assertTextEquals(sampleTootPreview.formattedReblogCount)
  }

  @Test
  fun shareActionIsNonexistentWhenLoading() {
    composeRule.setContent { OrcaTheme { TootPreview() } }
    composeRule.onTootPreviewShareAction().assertDoesNotExist()
  }

  @Test
  fun shareActionIsShownWhenLoaded() {
    composeRule.setContent {
      OrcaTheme { SampleTootPreview(relativeTimeProvider = TestRelativeTimeProvider) }
    }
    composeRule.onTootPreviewShareAction().assertIsDisplayed()
  }
}
