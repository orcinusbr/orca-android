package com.jeanbarrossilva.orca.platform.ui.component.avatar

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.jeanbarrossilva.loadable.placeholder.test.assertIsLoading
import com.jeanbarrossilva.loadable.placeholder.test.assertIsNotLoading
import com.jeanbarrossilva.orca.platform.ui.component.avatar.provider.test.rememberTestAvatarImageProvider
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
internal class SmallAvatarTests {
    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun isTaggedWhenEmpty() {
        composeRule.setContent { SmallAvatar() }
        composeRule.onNodeWithTag(AVATAR_TAG).assertExists()
    }

    @Test
    fun isLoadingWhenEmpty() {
        composeRule.setContent { SmallAvatar() }
        composeRule.onNodeWithTag(AVATAR_TAG).assertIsLoading()
    }

    @Test
    fun isTaggedWhenPopulated() {
        composeRule.setContent {
            SmallAvatar(
                Avatar.sample.name,
                Avatar.sample.url,
                imageProvider = rememberTestAvatarImageProvider()
            )
        }
        composeRule.onNodeWithTag(AVATAR_TAG).assertExists()
    }

    @Test
    fun isLoadedWhenPopulated() {
        composeRule.setContent {
            SmallAvatar(
                Avatar.sample.name,
                Avatar.sample.url,
                imageProvider = rememberTestAvatarImageProvider()
            )
        }
        composeRule.onNodeWithTag(AVATAR_TAG).assertIsNotLoading()
    }
}
