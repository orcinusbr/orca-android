package com.jeanbarrossilva.orca.platform.ui.component.avatar

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.jeanbarrossilva.loadable.placeholder.test.assertIsLoading
import com.jeanbarrossilva.loadable.placeholder.test.assertIsNotLoading
import com.jeanbarrossilva.orca.platform.ui.component.AVATAR_TAG
import com.jeanbarrossilva.orca.platform.ui.component.Avatar
import com.jeanbarrossilva.orca.platform.ui.component.SmallAvatar
import com.jeanbarrossilva.orca.std.imageloader.test.TestImageLoader
import org.junit.Rule
import org.junit.Test

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
                imageLoader = TestImageLoader
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
                imageLoader = TestImageLoader
            )
        }
        composeRule.onNodeWithTag(AVATAR_TAG).assertIsNotLoading()
    }
}
