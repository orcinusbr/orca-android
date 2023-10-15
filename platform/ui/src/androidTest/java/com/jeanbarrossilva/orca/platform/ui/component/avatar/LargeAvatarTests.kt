package com.jeanbarrossilva.orca.platform.ui.component.avatar

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.jeanbarrossilva.loadable.placeholder.test.assertIsLoading
import com.jeanbarrossilva.loadable.placeholder.test.assertIsNotLoading
import com.jeanbarrossilva.orca.platform.theme.OrcaTheme
import com.jeanbarrossilva.orca.std.imageloader.test.TestImageLoader
import org.junit.Rule
import org.junit.Test

internal class LargeAvatarTests {
  @get:Rule val composeRule = createComposeRule()

  @Test
  fun isTaggedWhenEmpty() {
    composeRule.setContent { OrcaTheme { LargeAvatar() } }
    composeRule.onNodeWithTag(AVATAR_TAG).assertExists()
  }

  @Test
  fun isLoadingWhenEmpty() {
    composeRule.setContent { OrcaTheme { LargeAvatar() } }
    composeRule.onNodeWithTag(AVATAR_TAG).assertIsLoading()
  }

  @Test
  fun isTaggedWhenPopulated() {
    composeRule.setContent { OrcaTheme { LargeAvatar(TestImageLoader, Avatar.sample.name) } }
    composeRule.onNodeWithTag(AVATAR_TAG).assertExists()
  }

  @Test
  fun isLoadedWhenPopulated() {
    composeRule.setContent { OrcaTheme { LargeAvatar(TestImageLoader, Avatar.sample.name) } }
    composeRule.onNodeWithTag(AVATAR_TAG).assertIsNotLoading()
  }
}
