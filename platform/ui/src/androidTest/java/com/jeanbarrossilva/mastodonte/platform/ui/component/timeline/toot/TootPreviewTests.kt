package com.jeanbarrossilva.mastodonte.platform.ui.component.timeline.toot

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import com.jeanbarrossilva.loadable.placeholder.test.assertIsLoading
import com.jeanbarrossilva.loadable.placeholder.test.assertIsNotLoading
import com.jeanbarrossilva.mastodonte.platform.theme.MastodonteTheme
import com.jeanbarrossilva.mastodonte.platform.ui.component.timeline.toot.time.test.TestRelativeTimeProvider
import com.jeanbarrossilva.mastodonte.platform.ui.test.onTootPreview
import org.junit.Rule
import org.junit.Test

internal class TootPreviewTests {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun isShownWhenLoading() {
        composeRule.setContent {
            MastodonteTheme {
                TootPreview()
            }
        }
        composeRule.onTootPreview().assertIsDisplayed()
    }

    @Test
    fun isShownWhenLoaded() {
        composeRule.setContent {
            MastodonteTheme {
                TestTootPreview()
            }
        }
        composeRule.onTootPreview().assertIsDisplayed()
    }

    @Test
    fun nameIsLoadingWhenLoading() {
        composeRule.setContent {
            MastodonteTheme {
                TootPreview()
            }
        }
        composeRule.onTootPreviewName().assertIsLoading()
    }

    @Test
    fun nameIsShownWhenLoaded() {
        composeRule.setContent {
            MastodonteTheme {
                TestTootPreview()
            }
        }
        composeRule.onTootPreviewName().assertIsNotLoading()
        composeRule.onTootPreviewName().assertTextEquals(TootPreview.sample.name)
    }

    @Test
    fun metadataIsLoadingWhenLoading() {
        composeRule.setContent {
            MastodonteTheme {
                TootPreview()
            }
        }
        composeRule.onTootPreviewMetadata().assertIsLoading()
    }

    @Test
    fun metadataIsShownWhenLoaded() {
        val relativeTimeProvider = TestRelativeTimeProvider()
        composeRule.setContent {
            MastodonteTheme {
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
            MastodonteTheme {
                TootPreview()
            }
        }
        composeRule.onTootPreviewBody().assertIsLoading()
    }

    @Test
    fun bodyIsShownWhenLoaded() {
        composeRule.setContent {
            MastodonteTheme {
                TestTootPreview()
            }
        }
        composeRule.onTootPreviewBody().assertIsNotLoading()
        composeRule.onTootPreviewBody().assertTextEquals(TootPreview.sample.body.text)
    }

    @Test
    fun commentCountStatIsNonexistentWhenLoading() {
        composeRule.setContent {
            MastodonteTheme {
                TootPreview()
            }
        }
        composeRule.onTootPreviewCommentCountStat().assertDoesNotExist()
    }

    @Test
    fun commentCountStatIsShownWhenLoaded() {
        composeRule.setContent {
            MastodonteTheme {
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
            MastodonteTheme {
                TootPreview()
            }
        }
        composeRule.onTootPreviewFavoriteCountStat().assertDoesNotExist()
    }

    @Test
    fun favoriteCountStatIsShownWhenLoaded() {
        composeRule.setContent {
            MastodonteTheme {
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
            MastodonteTheme {
                TootPreview()
            }
        }
        composeRule.onTootPreviewReblogCountStat().assertDoesNotExist()
    }

    @Test
    fun reblogCountStatIsShownWhenLoaded() {
        composeRule.setContent {
            MastodonteTheme {
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
            MastodonteTheme {
                TootPreview()
            }
        }
        composeRule.onTootPreviewShareAction().assertDoesNotExist()
    }

    @Test
    fun shareActionIsShownWhenLoaded() {
        composeRule.setContent {
            MastodonteTheme {
                TestTootPreview()
            }
        }
        composeRule.onTootPreviewShareAction().assertIsDisplayed()
    }
}
