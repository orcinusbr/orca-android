package com.jeanbarrossilva.orca.platform.ui.component.avatar

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.jeanbarrossilva.loadable.placeholder.test.assertIsLoading
import com.jeanbarrossilva.loadable.placeholder.test.assertIsNotLoading
import com.jeanbarrossilva.orca.core.feed.profile.Profile
import com.jeanbarrossilva.orca.platform.autos.theme.AutosTheme
import com.jeanbarrossilva.orca.platform.ui.core.createSample
import com.jeanbarrossilva.orca.std.imageloader.test.TestImageLoader
import org.junit.Rule
import org.junit.Test

internal class LargeAvatarTests {
  @get:Rule val composeRule = createComposeRule()

  @Test
  fun isTaggedWhenEmpty() {
    composeRule.setContent { AutosTheme { LargeAvatar() } }
    composeRule.onNodeWithTag(AVATAR_TAG).assertExists()
  }

  @Test
  fun isLoadingWhenEmpty() {
    composeRule.setContent { AutosTheme { LargeAvatar() } }
    composeRule.onNodeWithTag(AVATAR_TAG).assertIsLoading()
  }

  @Test
  fun isTaggedWhenPopulated() {
    composeRule.setContent {
      AutosTheme { LargeAvatar(TestImageLoader, Profile.createSample().name) }
    }
    composeRule.onNodeWithTag(AVATAR_TAG).assertExists()
  }

  @Test
  fun isLoadedWhenPopulated() {
    composeRule.setContent {
      AutosTheme { LargeAvatar(TestImageLoader, Profile.createSample().name) }
    }
    composeRule.onNodeWithTag(AVATAR_TAG).assertIsNotLoading()
  }
}
