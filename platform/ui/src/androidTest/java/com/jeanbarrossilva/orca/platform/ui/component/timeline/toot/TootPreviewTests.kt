package com.jeanbarrossilva.orca.platform.ui.component.timeline.toot

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.platform.app.InstrumentationRegistry
import com.jeanbarrossilva.loadable.placeholder.test.assertIsLoading
import com.jeanbarrossilva.loadable.placeholder.test.assertIsNotLoading
import com.jeanbarrossilva.orca.platform.theme.OrcaTheme
import com.jeanbarrossilva.orca.platform.theme.configuration.colors.Colors
import com.jeanbarrossilva.orca.platform.ui.component.timeline.toot.test.TestTootPreview
import com.jeanbarrossilva.orca.platform.ui.component.timeline.toot.test.onStatLabel
import com.jeanbarrossilva.orca.platform.ui.component.timeline.toot.test.onTootPreviewBody
import com.jeanbarrossilva.orca.platform.ui.component.timeline.toot.test.onTootPreviewCommentCountStat
import com.jeanbarrossilva.orca.platform.ui.component.timeline.toot.test.onTootPreviewFavoriteCountStat
import com.jeanbarrossilva.orca.platform.ui.component.timeline.toot.test.onTootPreviewMetadata
import com.jeanbarrossilva.orca.platform.ui.component.timeline.toot.test.onTootPreviewName
import com.jeanbarrossilva.orca.platform.ui.component.timeline.toot.test.onTootPreviewReblogCountStat
import com.jeanbarrossilva.orca.platform.ui.component.timeline.toot.test.onTootPreviewShareAction
import com.jeanbarrossilva.orca.platform.ui.component.timeline.toot.time.test.TestRelativeTimeProvider
import com.jeanbarrossilva.orca.platform.ui.test.component.timeline.toot.onTootPreview
import org.junit.Rule
import org.junit.Test

internal class TootPreviewTests {
    @get:Rule
    val composeRule = createComposeRule()

    private val sampleTootPreview
        get() = TootPreview.getSample(
            Colors.getDefault(InstrumentationRegistry.getInstrumentation().context)
        )

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
        composeRule.onTootPreviewName().assertTextEquals(sampleTootPreview.name)
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
            sampleTootPreview.getMetadata(relativeTimeProvider)
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
        composeRule.onTootPreviewBody().assertTextEquals(sampleTootPreview.body.text)
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
            sampleTootPreview.formattedCommentCount
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
            sampleTootPreview.formattedFavoriteCount
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
            sampleTootPreview.formattedReblogCount
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
