/*
 * Copyright © 2023 Orca
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see https://www.gnu.org/licenses.
 */

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

internal class SmallAvatarTests {
  @get:Rule val composeRule = createComposeRule()

  @Test
  fun isTaggedWhenEmpty() {
    composeRule.setContent { AutosTheme { SmallAvatar() } }
    composeRule.onNodeWithTag(AVATAR_TAG).assertExists()
  }

  @Test
  fun isLoadingWhenEmpty() {
    composeRule.setContent { AutosTheme { SmallAvatar() } }
    composeRule.onNodeWithTag(AVATAR_TAG).assertIsLoading()
  }

  @Test
  fun isTaggedWhenPopulated() {
    composeRule.setContent {
      AutosTheme { SmallAvatar(TestImageLoader, Profile.createSample().name) }
    }
    composeRule.onNodeWithTag(AVATAR_TAG).assertExists()
  }

  @Test
  fun isLoadedWhenPopulated() {
    composeRule.setContent {
      AutosTheme { SmallAvatar(TestImageLoader, Profile.createSample().name) }
    }
    composeRule.onNodeWithTag(AVATAR_TAG).assertIsNotLoading()
  }
}
