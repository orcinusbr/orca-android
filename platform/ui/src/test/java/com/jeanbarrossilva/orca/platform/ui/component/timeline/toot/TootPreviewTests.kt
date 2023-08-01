package com.jeanbarrossilva.orca.platform.ui.component.timeline.toot

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import com.jeanbarrossilva.loadable.placeholder.test.assertIsLoading
import com.jeanbarrossilva.loadable.placeholder.test.assertIsNotLoading
import com.jeanbarrossilva.orca.platform.theme.OrcaTheme
import com.jeanbarrossilva.orca.platform.ui.component.timeline.toot.time.test.TestRelativeTimeProvider
import com.jeanbarrossilva.orca.platform.ui.test.component.timeline.toot.onTootPreview
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class TootPreviewTests {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun isShownWhenLoading() {
        composeRule.setContent {
            OrcaTheme {
                TootPreview()
            }
        }
        composeRule.onTootPreview().assertIsDisplayed()
    }

    @Test
    fun isShownWhenLoaded() {
        composeRule.setContent {
            OrcaTheme {
                TestTootPreview()
            }
        }
        composeRule.onTootPreview().assertIsDisplayed()
    }

    @Test
    fun nameIsLoadingWhenLoading() {
        composeRule.setContent {
            OrcaTheme {
                TootPreview()
            }
        }
        composeRule.onTootPreviewName().assertIsLoading()
    }

    @Test
    fun nameIsShownWhenLoaded() {
        composeRule.setContent {
            OrcaTheme {
                TestTootPreview()
            }
        }
        composeRule.onTootPreviewName().assertIsNotLoading()
        composeRule.onTootPreviewName().assertTextEquals(TootPreview.sample.name)
    }

    @Test
    fun metadataIsLoadingWhenLoading() {
        composeRule.setContent {
            OrcaTheme {
                TootPreview()
            }
        }
        composeRule.onTootPreviewMetadata().assertIsLoading()
    }

    @Test
    fun metadataIsShownWhenLoaded() {
        val relativeTimeProvider = TestRelativeTimeProvider()
        composeRule.setContent {
            OrcaTheme {
                TestTootPreview(relativeTimeProvider = relativeTimeProvider)
            }
        }
        composeRule.onTootPreviewMetadata().assertIsNotLoading()
        composeRule.onTootPreviewMetadata().assertTextEquals(
            TootPreview.sample.getMetadata(relativeTimeProvider)
        )
    }

    @Test
    fun bodyIsLoadingWhenLoading() {
        composeRule.setContent {
            OrcaTheme {
                TootPreview()
            }
        }
        composeRule.onTootPreviewBody().assertIsLoading()
    }

    @Test
    fun bodyIsShownWhenLoaded() {
        composeRule.setContent {
            OrcaTheme {
                TestTootPreview()
            }
        }
        composeRule.onTootPreviewBody().assertIsNotLoading()
        composeRule.onTootPreviewBody().assertTextEquals(TootPreview.sample.body.text)
    }

    @Test
    fun commentCountStatIsNonexistentWhenLoading() {
        composeRule.setContent {
            OrcaTheme {
                TootPreview()
            }
        }
        composeRule.onTootPreviewCommentCountStat().assertDoesNotExist()
    }

    @Test
    fun commentCountStatIsShownWhenLoaded() {
        composeRule.setContent {
            OrcaTheme {
                TestTootPreview()
            }
        }
        composeRule.onTootPreviewCommentCountStat().assertIsDisplayed()
        composeRule.onTootPreviewCommentCountStat().onStatLabel().assertTextEquals(
            TootPreview.sample.formattedCommentCount
        )
    }

    @Test
    fun favoriteCountStatIsNonexistentWhenLoading() {
        composeRule.setContent {
            OrcaTheme {
                TootPreview()
            }
        }
        composeRule.onTootPreviewFavoriteCountStat().assertDoesNotExist()
    }

    @Test
    fun favoriteCountStatIsShownWhenLoaded() {
        composeRule.setContent {
            OrcaTheme {
                TestTootPreview()
            }
        }
        composeRule.onTootPreviewFavoriteCountStat().assertIsDisplayed()
        composeRule.onTootPreviewFavoriteCountStat().onStatLabel().assertTextEquals(
            TootPreview.sample.formattedFavoriteCount
        )
    }

    @Test
    fun reblogCountStatIsNonexistentWhenLoading() {
        composeRule.setContent {
            OrcaTheme {
                TootPreview()
            }
        }
        composeRule.onTootPreviewReblogCountStat().assertDoesNotExist()
    }

    @Test
    fun reblogCountStatIsShownWhenLoaded() {
        composeRule.setContent {
            OrcaTheme {
                TestTootPreview()
            }
        }
        composeRule.onTootPreviewReblogCountStat().assertIsDisplayed()
        composeRule.onTootPreviewReblogCountStat().onStatLabel().assertTextEquals(
            TootPreview.sample.formattedReblogCount
        )
    }

    @Test
    fun shareActionIsNonexistentWhenLoading() {
        composeRule.setContent {
            OrcaTheme {
                TootPreview()
            }
        }
        composeRule.onTootPreviewShareAction().assertDoesNotExist()
    }

    @Test
    fun shareActionIsShownWhenLoaded() {
        composeRule.setContent {
            OrcaTheme {
                TestTootPreview()
            }
        }
        composeRule.onTootPreviewShareAction().assertIsDisplayed()
    }
}
